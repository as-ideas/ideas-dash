package de.axelspringer.ideas.tools.dash.business.fabric;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * Generic client for fabric/crashlytics
 */
@Service
public class FabricService {

    @Autowired
    @Qualifier("exceptionSwallowingRestTemplate")
    private RestTemplate restTemplate;

    public FabricAuth logIn(String email, String password) throws FabricExecutionException {

        // load login-page
        final ResponseEntity<String> loginFormResponse = restTemplate.getForEntity("https://fabric.io/login", String.class);
        if (loginFormResponse.getStatusCode() != HttpStatus.OK) {
            throw new FabricExecutionException("01 login page load failed");
        }

        // headers for requests
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Cookie", extractCookie(loginFormResponse));
        httpHeaders.add("X-CSRF-Token", extractToken(loginFormResponse.getBody()));

        // login-form
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("email", email);
        formData.add("password", password);

        // submit login/create session
        final HttpEntity<MultiValueMap> sessionRequest = new HttpEntity<>(formData, httpHeaders);
        final ResponseEntity<String> sessionResponse = restTemplate.postForEntity("https://fabric.io/api/v2/session", sessionRequest, String.class);
        if (sessionResponse.getStatusCode() != HttpStatus.CREATED) {
            throw new FabricExecutionException("02 Expected HTTP Status CREATED but got " + sessionResponse.getStatusCode());
        }

        // a new cookie will be assigned after session create, put it in the headers
        httpHeaders.set("Cookie", extractCookie(sessionResponse));

        // we also need the developer token...
        final ResponseEntity<FabricConfig> fabricConfigResponse = restTemplate.exchange("https://fabric.io/api/v2/client_boot/config_data", HttpMethod.GET, new HttpEntity<>(httpHeaders), FabricConfig.class);
        if (fabricConfigResponse.getStatusCode() != HttpStatus.OK) {
            throw new FabricExecutionException("03 Expected HTTP Status OK but got " + fabricConfigResponse.getStatusCode());
        }

        httpHeaders.add("X-CRASHLYTICS-DEVELOPER-TOKEN", fabricConfigResponse.getBody().getDeveloper_token());

        return new FabricAuth(httpHeaders);
    }

    String extractToken(String markup) throws FabricExecutionException {

        final Document document = Jsoup.parse(markup);
        final Element tokenElement = document.select("[name=\"csrf-token\"]").first();
        if (tokenElement == null) {
            throw new FabricExecutionException("05 error parsing token from markup.");
        }
        return tokenElement.attr("content");
    }

    private String extractCookie(ResponseEntity response) throws FabricExecutionException {

        final List<String> setCookies = response.getHeaders().get("Set-Cookie");
        if (setCookies.size() != 1) {
            throw new FabricExecutionException("expected 1 cookie to set, found " + setCookies.size());
        }
        return setCookies.get(0);
    }

    public List<FabricApp> loadApps(FabricAuth fabricAuth) throws FabricExecutionException {

        final HttpHeaders headers = fabricAuth.getHeaders();
        final String url = "https://fabric.io/api/v2/apps?include=sdk_kits";

        final ResponseEntity<FabricApp[]> appsResponse = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), FabricApp[].class);

        if (appsResponse.getStatusCode() != HttpStatus.OK) {
            throw new FabricExecutionException("04 Expected HTTP OK but got " + appsResponse.getStatusCode());
        }

        return Arrays.asList(appsResponse.getBody());
    }

    public List<FabricIssue> loadIssues(String fabricAppId, FabricAuth fabricAuth) {

        final HttpHeaders httpHeaders = fabricAuth.getHeaders();
        final String url = "https://fabric.io/api/v3/projects/" + fabricAppId + "/issues?state=open&event_type=all";

        final ResponseEntity<FabricIssue[]> issuesResponse = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), FabricIssue[].class);

        if (issuesResponse.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException(new FabricExecutionException("05 Expected HTTP OK but got " + issuesResponse.getStatusCode()));
        }

        if (!issuesResponse.hasBody()) {
            throw new RuntimeException(new FabricExecutionException("Expected Body but got nothing."));
        }

        return Arrays.asList(issuesResponse.getBody());
    }


    public List<FabricIssueNote> loadNotes(String fabricAppId, String fabricIssueId, FabricAuth fabricAuth) {

        final HttpHeaders headers = fabricAuth.getHeaders();
        final String url = "https://fabric.io/api/v3/projects/" + fabricAppId + "/issues/" + fabricIssueId + "/notes";

        final ResponseEntity<FabricIssueNote[]> notesResponse = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), FabricIssueNote[].class);
        ;

        if (notesResponse.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException(new FabricExecutionException("05 Expected HTTP OK but got " + notesResponse.getStatusCode()));
        }

        if (!notesResponse.hasBody()) {
            throw new RuntimeException(new FabricExecutionException("Expected Body but got nothing."));
        }

        return Arrays.asList(notesResponse.getBody());
    }
}
