package de.axelspringer.ideas.tools.dash.business.jenkins;

import com.google.gson.Gson;
import de.axelspringer.ideas.tools.dash.util.RestClient;
import org.apache.http.auth.AuthenticationException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JenkinsClient {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(JenkinsClient.class);
    @Autowired
    private RestClient restClient;

    @Autowired
    private Gson gson;

    public <T> T query(String url, String username, String password, Class<T> responseType) throws IOException, AuthenticationException {

        String apiUrl = url + "/api/json";
        log.debug("Querying jenkins URL {}", apiUrl);

        String response = restClient.get(apiUrl, username, password, null);
        return gson.fromJson(response, responseType);
    }
}
