package de.axelspringer.ideas.tools.dash.business.datadog;

import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.check.CheckExecutor;
import de.axelspringer.ideas.tools.dash.business.check.checkresult.CheckResult;
import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.business.customization.Team;
import de.axelspringer.ideas.tools.dash.presentation.State;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DataDogCheckExecutor implements CheckExecutor<DataDogCheck> {

    private static final Logger LOG = LoggerFactory.getLogger(DataDogCheckExecutor.class);

    private final String DATADOG_API_URL = "https://app.datadoghq.com/api/v1";
    private final String DATADOG_MONITORS_ENDPOINT_URL = DATADOG_API_URL + "/monitor";

    @Autowired
    @Qualifier("exceptionSwallowingRestTemplate")
    private RestTemplate restTemplate;

    @Override
    public List<CheckResult> executeCheck(DataDogCheck check) {

        final ResponseEntity<DataDogMonitor[]> monitorResponse = loadFromDataDog(check.getApiKey(), check.getAppKey(), check.getNameFilter());

        if (monitorResponse.getStatusCode() != HttpStatus.OK) {
            return Collections.singletonList(new CheckResult(State.RED, "DataDog", "got http " + monitorResponse.getStatusCode(), 1, 1, check.getGroup()).
                    withTeams(Collections.singletonList(check.getTeamMapping())));
        }

        return Arrays.stream(monitorResponse.getBody())
                .filter(candidate -> !check.isBlacklisted(candidate.getName()))
                .map(monitor -> convertMonitorToCheckResult(monitor, check))
                .collect(Collectors.toList());
    }

    private ResponseEntity<DataDogMonitor[]> loadFromDataDog(String apiKey, String appKey, String nameFilter) {
        try {
            URI url = buildDatadogApiUrl(apiKey, appKey, nameFilter);
            return restTemplate.getForEntity(url, DataDogMonitor[].class);
        } catch (URISyntaxException e) {
            LOG.error("Could not build DataDog API Url [args: name={}]", nameFilter, e);
            throw new RuntimeException(e);
        }

    }

    private URI buildDatadogApiUrl(String apiKey, String appKey, String nameFilter) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(DATADOG_MONITORS_ENDPOINT_URL);
        uriBuilder.setParameter("api_key", apiKey);
        uriBuilder.setParameter("application_key", appKey);
        uriBuilder.setParameter("with_downtimes", "true");

        if (!StringUtils.isEmpty(nameFilter)) {
            uriBuilder.setParameter("name", nameFilter);
        }

        return uriBuilder.build();
    }

    CheckResult convertMonitorToCheckResult(DataDogMonitor monitor, DataDogCheck check) {

        Group group = check.getGroup();

        String infoMessage = monitor.getOverallState() + " (query: " + monitor.getQuery() + ")";

        State state = check.getTriggeredStateMapper().map(monitor);

        if (monitor.isOverallStateOk()) {
            state = State.GREEN;
        } else if (monitor.hasActiveDowntime()) {
			state = State.GREEN;
			infoMessage = "MAINTENANCE!";
		} else if (monitor.isSilencedMonitor()) {
            state = State.GREEN;
            infoMessage = "NOT ACTIVE (silenced)!";
        }

        final CheckResult checkResult = new CheckResult(state, monitor.getName() + "@DataDog", infoMessage, 1, State.GREEN == state ? 0 : 1, group);
        final List<Team> teams = decideTeams(monitor.getName(), check.getJobNameTeamMappings(), check.getTeamMapping());
        if (teams != null && !teams.isEmpty()) {
            checkResult.withTeams(teams);
        }

        // HINT https://app.datadoghq.com/monitors#status?id=182437&group=all
        checkResult.withLink("https://app.datadoghq.com/monitors#status?id=" + monitor.getId() + "&group=all");
        return checkResult;
    }

    List<Team> decideTeams(String monitorName, Map<String, List<Team>> jobNameTeamMappings, Team teamMapping) {

        // operate on this string
        monitorName = monitorName.toLowerCase();

        // search for team mapping
        for (String teamName : jobNameTeamMappings.keySet()) {
            if (monitorName.contains(teamName.toLowerCase())) {
                return jobNameTeamMappings.get(teamName);
            }
        }
        return teamMapping != null ? Collections.singletonList(teamMapping) : null;
    }

    @Override
    public boolean isApplicable(Check check) {
        return check instanceof DataDogCheck;
    }
}
