package de.axelspringer.ideas.tools.dash.business.jenkins;

import com.google.gson.Gson;
import de.axelspringer.ideas.tools.dash.util.RestClient;
import org.apache.http.auth.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JenkinsClient {

    private static final Logger LOG = LoggerFactory.getLogger(JenkinsClient.class);

    private static final int THIRTY_SECONDS_IN_MS = 30 * 1000;

    @Autowired
    private RestClient restClient;

    @Autowired
    private Gson gson;

    public <T> T query(String url, String username, String password, Class<T> responseType) throws IOException, AuthenticationException {

        String apiUrl = url + "/api/json";
        LOG.debug("Querying jenkins URL {}", apiUrl);

        String response = restClient.create()
                .withTimeout(THIRTY_SECONDS_IN_MS)
                .withCredentials(username, password)
                .get(apiUrl, null);
        return gson.fromJson(response, responseType);
    }
}
