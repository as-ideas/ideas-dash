package de.axelspringer.ideas.tools.dash.util;

import java.io.IOException;
import java.util.Map;

import org.apache.http.auth.AuthenticationException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestClient {

    @Autowired
    private CloseableHttpClient httpClient;

    public CloseableHttpClientRestClient create() {
        return new CloseableHttpClientRestClient(httpClient);
    }

    /**
     * @deprecated Use restClient.create().withHeader(requestParams).get(url) instead
     *
     * @param url           The url to send the request to
     * @param userName      The username to use for basic auth
     * @param password      The password to use for basic auth
     * @param requestParams The query string parameters
     * @return The response body
     * @throws IOException             Thrown when something wrong happened on the wire
     * @throws AuthenticationException Thrown when authentication failed
     */
    @Deprecated
    public String get(String url, String userName, String password, Map<String, String> requestParams) throws IOException, AuthenticationException {
        return create().withCredentials(userName, password).withHeaders(requestParams).get(url);
    }

    public String get(String url) throws IOException {
        return create().get(url);
    }
}
