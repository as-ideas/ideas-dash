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

    @Autowired
    private CloseableHttpClient httpClientWithExtraPoodle;


    public CloseableHttpClientRestClient create() {
        return new CloseableHttpClientRestClient(httpClient);
    }

    public CloseableHttpClientRestClient createPoodleClient() {
        return new CloseableHttpClientRestClient(httpClientWithExtraPoodle);
    }

    /**
     * @deprecated Use restClient.create().withHeader(requestParams).get(url) instead
     */
    @Deprecated
    public String get(String url, String userName, String password, Map<String, String> requestParams) throws IOException, AuthenticationException {
        return create().withCredentials(userName, password).withHeaders(requestParams).get(url);
    }

    public String get(String url) throws IOException {
       return create().get(url);
    }
}
