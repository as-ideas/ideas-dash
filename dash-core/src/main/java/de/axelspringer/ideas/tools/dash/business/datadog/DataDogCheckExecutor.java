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
        String nameFilter = check.getNameFilter();
        Map<String, Team> jobNameTeamMappings = check.getJobNameTeamMappings();

        State state = monitor.isOverallStateOk() ? State.GREEN : State.RED;
        String infoMessage = monitor.getOverallState() + " (query: " + monitor.getQuery() + ")";
        final int errorCount = State.GREEN == state ? 0 : 1;

        if (monitor.isSilencedMonitor()) {
            state = State.GREEN;
            infoMessage = "NOT ACTIVE!";
        } else if (downtimes.hasDowntime(monitor)) {
            state = State.GREEN;
            infoMessage = "MAINTENANCE!";
        }

        final CheckResult checkResult = new CheckResult(state, monitor.getName() + "@DataDog", infoMessage, 1, errorCount, group);
        final Team team = decideTeam(monitor.getName(), nameFilter, jobNameTeamMappings);
        if (team != null) {
            checkResult.withTeam(team);
        }

        checkResult.withLink("https://www.datadoghq.com/");
        return checkResult;
    }

    Team decideTeam(String monitorName, String nameFilter, Map<String, Team> jobNameTeamMappings) {

        // operate on this string
        String strippedName = monitorName.toLowerCase();

        // remove name filter
        strippedName = nameFilter != null && strippedName.startsWith(nameFilter.toLowerCase()) ? strippedName.substring(nameFilter.length()) : strippedName;

        // search for team mapping
        for (String teamName : jobNameTeamMappings.keySet()) {
            if (strippedName.startsWith(teamName.toLowerCase())) {
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
