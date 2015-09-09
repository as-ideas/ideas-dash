package de.axelspringer.ideas.tools.dash.business.datadog;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DataDogDowntimes {

    private final List<DataDogDowntime> downtimes;

    public DataDogDowntimes(RestTemplate restTemplate, String apiKey, String appKey) {
        final String url = "https://app.datadoghq.com/api/v1/downtime?current_only=true&api_key=" + apiKey + "&application_key=" + appKey;
        final ResponseEntity<DataDogDowntime[]> downtimeResponse = restTemplate.getForEntity(url, DataDogDowntime[].class);

        if (downtimeResponse.getStatusCode() != HttpStatus.OK) {
            downtimes = Collections.emptyList();
        } else {
            downtimes = Arrays.asList(downtimeResponse.getBody());
        }
    }

    // HINT Downtimes means the Monitor needs to have ALL THE TAGS OF THE DOWNTIME --> "scope" of downtimes are linked with AND
    public boolean hasDowntime(DataDogMonitor monitor) {
        for (DataDogDowntime downtime : downtimes) {
            if (monitor.hasAllTags(downtime.scope)) {
                return true;
            }
        }
        return false;
    }

    public List<DataDogDowntime> getDowntimes() {
        return downtimes;
    }
}
