package de.axelspringer.ideas.tools.dash.business.jenkins;

import com.google.gson.Gson;
import de.axelspringer.ideas.tools.dash.util.RestClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.auth.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class JenkinsClient {

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
