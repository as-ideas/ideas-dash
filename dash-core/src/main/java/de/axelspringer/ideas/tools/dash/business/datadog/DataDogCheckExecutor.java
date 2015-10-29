package de.axelspringer.ideas.tools.dash.business.datadog;

import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.check.CheckExecutor;
import de.axelspringer.ideas.tools.dash.business.check.CheckResult;
import de.axelspringer.ideas.tools.dash.business.datadog.monitorresultmapper.MonitorResultMapper;
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
import java.util.stream.Collectors;

@Service
public class DataDogCheckExecutor implements CheckExecutor<DataDogCheck> {

    @Autowired
    @Qualifier("exceptionSwallowingRestTemplate")
    private RestTemplate restTemplate;

    @Autowired
    private MonitorResultMapper monitorResultMapper;

    @Override
    public List<CheckResult> executeCheck(DataDogCheck check) {

        final ResponseEntity<DataDogMonitor[]> monitorResponse = loadFromDataDog(check.getApiKey(), check.getAppKey());
        final DataDogDowntimes downtimes = new DataDogDowntimes(restTemplate, check.getApiKey(), check.getAppKey());

        if (monitorResponse.getStatusCode() != HttpStatus.OK) {
            return Collections.singletonList(new CheckResult(State.RED, "DataDog", "got http " + monitorResponse.getStatusCode(), 1, 1, check.getGroup()).withTeam(check.getTeam()));
        }

        return Arrays.asList(monitorResponse.getBody()).stream()
                .filter(candidate -> isNameMatching(check, candidate))
                .map(monitor -> monitorResultMapper.mapToResult(monitor, check, downtimes))
                .collect(Collectors.toList());
    }

    private ResponseEntity<DataDogMonitor[]> loadFromDataDog(String apiKey, String appKey) {
        final String url = "https://app.datadoghq.com/api/v1/monitor?api_key=" + apiKey + "&application_key=" + appKey;
        return restTemplate.getForEntity(url, DataDogMonitor[].class);
    }


    private boolean isNameMatching(DataDogCheck check, DataDogMonitor candidate) {
        return StringUtils.isEmpty(check.getNameFilter()) || candidate.getName().toLowerCase().contains(check.getNameFilter().toLowerCase());
    }

    @Override
    public boolean isApplicable(Check check) {
        return check instanceof DataDogCheck;
    }
}
