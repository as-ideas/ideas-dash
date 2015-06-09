package de.axelspringer.ideas.tools.dash.business.datadog;

import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.check.CheckExecutor;
import de.axelspringer.ideas.tools.dash.business.check.CheckResult;
import de.axelspringer.ideas.tools.dash.presentation.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataDogCheckExecutor implements CheckExecutor<DataDogCheck> {

    @Autowired
    @Qualifier("exceptionSwallowingRestTemplate")
    private RestTemplate restTemplate;

    @Override
    public List<CheckResult> executeCheck(DataDogCheck check) {

        final String url = "https://app.datadoghq.com/api/v1/monitor?api_key=" + check.getApiKey() + "&application_key=" + check.getAppKey();

        final ResponseEntity<DataDogMonitor[]> monitorResponse = restTemplate.getForEntity(url, DataDogMonitor[].class);

        if (monitorResponse.getStatusCode() != HttpStatus.OK) {
            return Collections.singletonList(new CheckResult(State.RED, "DataDog", "got http " + monitorResponse.getStatusCode(), 1, 1, check.getGroup()).withTeam(check.getTeam()));
        }

        final List<DataDogMonitor> dataDogMonitors = Arrays.asList(monitorResponse.getBody());
        return dataDogMonitors.stream()
                .filter(candidate -> candidate.getName().toLowerCase().contains(check.getNameFilter().toLowerCase()))
                .map(monitor -> map(monitor, check))
                .collect(Collectors.toList());
    }

    private CheckResult map(DataDogMonitor monitor, DataDogCheck check) {

        final boolean ok = DataDogMonitor.STATE_OK.equals(monitor.getOverall_state());
        return new CheckResult(ok ? State.GREEN : State.RED, monitor.getName() + "@DataDog", monitor.getOverall_state() + " (query: " + monitor.getQuery() + ")", 1, ok ? 0 : 1, check.getGroup())
                .withTeam(check.getTeam())
                .withLink("https://www.datadoghq.com/");
    }

    @Override
    public boolean isApplicable(Check check) {
        return check instanceof DataDogCheck;
    }
}
