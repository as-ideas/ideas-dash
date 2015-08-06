package de.axelspringer.ideas.tools.dash.util;

import org.apache.http.HttpStatus;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class CloseableHttpClientRestClient {

    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(CloseableHttpClientRestClient.class);

    public static final int TEN_SECONS_IN_MS = 10 * 1000;
    public static final int THIRTY_SECONS_IN_MS = 30 * 1000;

    private CloseableHttpClient httpClient;
    private int timeoutInMs = TEN_SECONS_IN_MS;
    private String username;
    private String password;
    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> queryParameters = new HashMap<>();

    public CloseableHttpClientRestClient(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public CloseableHttpClientRestClient withTimeout(int timeoutInMs) {
        this.timeoutInMs = timeoutInMs;
        return this;
    }

    public CloseableHttpClientRestClient withCredentials(String username, String password) {
        this.username = username;
        this.password = password;
        return this;
    }

    public CloseableHttpClientRestClient withHeaders(Map<String, String> headerEntries) {
        for (Map.Entry<String, String> entry : headerEntries.entrySet()) {
            withHeader(entry.getKey(), entry.getValue());
        }
        return this;
    }

    public CloseableHttpClientRestClient withHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }

    public CloseableHttpClientRestClient withQueryParameter(String key, String value) {
        queryParameters.put(key, value);
        return this;
    }

    public CloseableHttpClientRestClient withQueryParameters(Map<String, String> entries) {
        for (Map.Entry<String, String> entry : entries.entrySet()) {
            withQueryParameter(entry.getKey(), entry.getValue());
        }
        return this;
    }

    public String get(String url) {
        try {
            final HttpGet httpGet = new HttpGet(buildUrl(url, queryParameters));

            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpGet.addHeader(entry.getKey(), entry.getValue());
            }

            // basic auth
            if (!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)) {
                httpGet.addHeader(new BasicScheme().authenticate(new UsernamePasswordCredentials(username, password), httpGet));
            }

            final RequestConfig.Builder configBuilder = RequestConfig.custom()
                    .setConnectTimeout(timeoutInMs)
                    .setSocketTimeout(timeoutInMs)
                    .setConnectionRequestTimeout(timeoutInMs);

            httpGet.setConfig(configBuilder.build());

            try (CloseableHttpResponse httpResponse = httpClient.execute(httpGet)) {
                if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    return EntityUtils.toString(httpResponse.getEntity());
                }
                LOG.warn("Error [Status=" + httpResponse.getStatusLine().getStatusCode() + " , Url=" + url + ", Body=" + EntityUtils.toString(httpResponse.getEntity()) + "]");
                return "";
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private String buildUrl(String url, Map<String, String> params) {

        if (params == null || params.isEmpty()) {
            return url;
        }

        url = url + "?";

        for (String key : params.keySet()) {
            String value = params.get(key);
            url = url.endsWith("?") ? url : url + "&";
            try {
                url = url + URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }

        return url;
    }

}
