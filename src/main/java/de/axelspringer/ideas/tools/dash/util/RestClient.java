package de.axelspringer.ideas.tools.dash.util;

import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Map;

@Service
public class RestClient {

    public static final int CONNECTION_REQUEST_TIMEOUT = 16 * 1000;
    public static final int SOCKET_TIMEOUT = 16 * 1000;

    @Autowired
    private CloseableHttpClient httpClient;

    @SuppressWarnings("deprecation")
    public String get(String url, String userName, String password, Map<String, String> requestParams) throws IOException, AuthenticationException {

        final HttpGet httpGet = new HttpGet(buildUrl(url, requestParams));

        // add pwl-header
        httpGet.addHeader("X-ESBClient", "pwl");

        // basic auth
        if (!StringUtils.isEmpty(userName) && !StringUtils.isEmpty(password)) {
            httpGet.addHeader(new BasicScheme().authenticate(new UsernamePasswordCredentials(userName, password), httpGet));
        }

        final RequestConfig.Builder configBuilder = RequestConfig.custom()
                .setConnectTimeout(CONNECTION_REQUEST_TIMEOUT)
                .setSocketTimeout(SOCKET_TIMEOUT)
                .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT);

        httpGet.setConfig(configBuilder.build());

        try (CloseableHttpResponse httpResponse = httpClient.execute(httpGet)) {
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return EntityUtils.toString(httpResponse.getEntity());
            }
            System.out.println("error");
            return "";
        }
    }

    public String get(String url) throws IOException {
        try {
            return get(url, null, null, null);
        } catch (AuthenticationException e) {
            throw new RuntimeException(e);
        }
    }

    public String getJsonp(String url, String callbackName, HttpHost proxy) throws URISyntaxException, IOException {
        URIBuilder uriBuilder = new URIBuilder(url);
        uriBuilder.addParameter("callback", callbackName);

        // result will be something like callbackName({"key": "value"});
        String result = get(uriBuilder.build().toString());

        // cut off the leading "callbackName(" and trailing ");"
        return result.substring(callbackName.length() + 1, result.length() - 2);
    }

    private String buildUrl(String url, Map<String, String> params) {

        if (params == null) {
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
