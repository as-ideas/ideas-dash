package de.axelspringer.ideas.tools.dash.business.datadog;

import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.check.CheckExecutor;
import de.axelspringer.ideas.tools.dash.business.check.CheckResult;
import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.business.customization.Team;
import de.axelspringer.ideas.tools.dash.presentation.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DataDogCheckExecutor implements CheckExecutor<DataDogCheck> {

    @Autowired
    @Qualifier("exceptionSwallowingRestTemplate")
    private RestTemplate restTemplate;

    @Override
    public List<CheckResult> executeCheck(DataDogCheck check) {


        final ResponseEntity<DataDogMonitor[]> monitorResponse = loadFromDataDog(check.getApiKey(), check.getAppKey());
        final DataDogDowntimes downtimes = new DataDogDowntimes(restTemplate, check.getApiKey(), check.getAppKey());

        if (monitorResponse.getStatusCode() != HttpStatus.OK) {
            return Collections.singletonList(new CheckResult(State.RED, "DataDog", "got http " + monitorResponse.getStatusCode(), 1, 1, check.getGroup()).withTeam(check.getTeam()));
        }

        return Arrays.asList(monitorResponse.getBody()).stream()
                .filter(candidate -> isNameMatching(check, candidate))
                .map(monitor -> convertMonitorToCheckResult(monitor, check, downtimes))
                .collect(Collectors.toList());
    }

    private ResponseEntity<DataDogMonitor[]> loadFromDataDog(String apiKey, String appKey) {
        final String url = "https://app.datadoghq.com/api/v1/monitor?api_key=" + apiKey + "&application_key=" + appKey;
        return restTemplate.getForEntity(url, DataDogMonitor[].class);
    }


    private boolean isNameMatching(DataDogCheck check, DataDogMonitor candidate) {
        return StringUtils.isEmpty(check.getNameFilter()) || candidate.getName().toLowerCase().contains(check.getNameFilter().toLowerCase());
    }

    CheckResult convertMonitorToCheckResult(DataDogMonitor monitor, DataDogCheck check, DataDogDowntimes downtimes) {
        Group group = check.getGroup();
        Map<String, Team> jobNameTeamMappings = check.getJobNameTeamMappings();

        String infoMessage = monitor.getOverallState() + " (query: " + monitor.getQuery() + ")";
        State state = State.RED;

        if (monitor.isOverallStateOk()) {
            state = State.GREEN;
        } else if (monitor.isSilencedMonitor()) {
            state = State.GREEN;
            infoMessage = "NOT ACTIVE (silenced)!";
        } else if (downtimes.hasDowntime(monitor)) {
            state = State.GREEN;
            infoMessage = "MAINTENANCE!";
        }

        final CheckResult checkResult = new CheckResult(state, monitor.getName() + "@DataDog", infoMessage, 1, State.GREEN == state ? 0 : 1, group);
        final Team team = decideTeam(monitor.getName(), jobNameTeamMappings);
        if (team != null) {
            checkResult.withTeam(team);
        }

        // HINT https://app.datadoghq.com/monitors#status?id=182437&group=all
        checkResult.withLink("https://app.datadoghq.com/monitors#status?id=" + monitor.getId() + "&group=all");
        return checkResult;
    }

    Team decideTeam(String monitorName, Map<String, Team> jobNameTeamMappings) {

        // operate on this string
        monitorName = monitorName.toLowerCase();

        // search for team mapping
        for (String teamName : jobNameTeamMappings.keySet()) {
            if (monitorName.contains(teamName.toLowerCase())) {
                return jobNameTeamMappings.get(teamName);
            }
        }
        return null;
    }

    @Override
    public boolean isApplicable(Check check) {
        return check instanceof DataDogCheck;
    }
}
