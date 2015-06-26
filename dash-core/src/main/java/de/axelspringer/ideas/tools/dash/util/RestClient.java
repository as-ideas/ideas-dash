package de.axelspringer.ideas.tools.dash.util;

import org.apache.http.auth.AuthenticationException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class RestClient {

    @Autowired
    private CloseableHttpClient httpClient;

    public CloseableHttpClientRestClient create() {
        return new CloseableHttpClientRestClient(httpClient);
    }

    public String get(String url, String userName, String password, Map<String, String> requestParams) throws IOException, AuthenticationException {
        return create().withCredentials(userName, password).get(url, requestParams);
    }

    public String get(String url) throws IOException {
       return create().get(url);
    }
}
