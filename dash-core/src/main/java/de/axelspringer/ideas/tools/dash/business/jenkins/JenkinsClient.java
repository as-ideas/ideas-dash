package de.axelspringer.ideas.tools.dash.business.jenkins;

import com.google.gson.Gson;
import de.axelspringer.ideas.tools.dash.util.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JenkinsClient {

    private static final Logger LOG = LoggerFactory.getLogger(JenkinsClient.class);

    private static final int THIRTY_SECONDS_IN_MS = 30 * 1000;

    @Autowired
    private RestClient restClient;

    @Autowired
    private Gson gson;

    public <T> T queryApi(String url, JenkinsServerConfiguration serverConfig, Class<T> responseType) {
        return query(url + "/api/json", serverConfig, responseType);
    }

    public <T> T queryWorkflowApi(String url, JenkinsServerConfiguration serverConfig, Class<T> responseType) {
        return query(url + "/wfapi/describe", serverConfig, responseType);
    }

    private <T> T query(String fullUrl, JenkinsServerConfiguration serverConfig, Class<T> responseType) {

        LOG.debug("Querying jenkins URL {}", fullUrl);

        String response = restClient.create()
                .withTimeout(THIRTY_SECONDS_IN_MS)
                .withCredentials(serverConfig.getUserName(), serverConfig.getApiToken())
                .get(fullUrl);
        return gson.fromJson(response, responseType);
    }
}
