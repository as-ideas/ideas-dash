package de.axelspringer.ideas.tools.dash.business.fabric;

import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.check.CheckExecutor;
import de.axelspringer.ideas.tools.dash.business.check.CheckResult;
import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.business.customization.Team;
import de.axelspringer.ideas.tools.dash.presentation.State;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Executor for {@link FabricCheck}
 */
@Service
public class FabricCheckExecutor implements CheckExecutor<FabricCheck> {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("exceptionSwallowingRestTemplate")
    private RestTemplate restTemplate;

    @Override
    public List<CheckResult> executeCheck(FabricCheck fabricCheck) {

        // log-in
        final HttpHeaders sessionHeaders;
        final Team team = fabricCheck.getTeam();
        final Group group = fabricCheck.getGroup();

        try {
            sessionHeaders = logIn(fabricCheck.getEmail(), fabricCheck.getPassword());
        } catch (FabricExecutionException e) {
            return Collections.singletonList(new CheckResult(State.RED, "FABRIC", e.getMessage(), 1, 1, group).withTeam(team));
        }

        // load apps
        final List<FabricApp> fabricApps;
        try {
            fabricApps = loadApps(sessionHeaders);
        } catch (FabricExecutionException e) {
            return Collections.singletonList(new CheckResult(State.RED, "FABRIC", e.getMessage(), 1, 1, group).withTeam(team));
        }

        return fabricApps.stream()
                .map(fabricApp -> loadIssues(fabricApp, sessionHeaders))
                .map(fabricAppWithCrashesCount -> map(fabricAppWithCrashesCount, group, team))
                .collect(Collectors.toList());
    }

    private CheckResult map(FabricAppWithCrashesCount fabricAppWithCrashesCount, Group group, Team team) {

        final FabricApp fabricApp = fabricAppWithCrashesCount.getFabricApp();
        final Integer crashesCount = fabricAppWithCrashesCount.getCrashesCount();

        // every crash/issue not found in development but out in the wild is considered real bad (red) to make dev team fix the issue immediately
        final State state = crashesCount > 0 ? State.RED : State.GREEN;
        return new CheckResult(state, fabricApp.getName(), crashesCount + " unresolved", 1 + crashesCount, crashesCount, group)
                .withLink(fabricApp.getDashboard_url())
                .withTeam(team);
    }

    private FabricAppWithCrashesCount loadIssues(FabricApp fabricApp, HttpHeaders httpHeaders) {

        final String appId = fabricApp.getId();

        ResponseEntity<FabricIssue[]> issuesResponse = restTemplate.exchange("https://fabric.io/api/v3/projects/" + appId + "/issues?state=open&event_type=all", HttpMethod.GET, new HttpEntity<>(httpHeaders), FabricIssue[].class);
        if (issuesResponse.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException(new FabricExecutionException("05 Expected HTTP OK but got " + issuesResponse.getStatusCode()));
        }

        if (!issuesResponse.hasBody()) {
            throw new RuntimeException(new FabricExecutionException("Expected Body but got nothing."));
        }

        int nonIgnoredIssuesCount = 0;

        for (FabricIssue fabricIssue : issuesResponse.getBody()) {
            if (!ignoreIssue(fabricIssue)) {
                nonIgnoredIssuesCount++;
            }
        }

        return new FabricAppWithCrashesCount(fabricApp, nonIgnoredIssuesCount);
    }

    private boolean ignoreIssue(FabricIssue fabricIssue) {

        return fabricIssue.getNotes_count() > 1 && loadNotes(fabricIssue).stream().anyMatch(issue -> issue.getBody().contains("#ignoreIssue"));
    }

    private List<FabricIssueNote> loadNotes(FabricIssue fabricIssue) {

        final ResponseEntity<FabricIssueNote[]> notesResponse = restTemplate.exchange()
        ...TODO
    }

    private List<FabricApp> loadApps(HttpHeaders httpHeaders) throws FabricExecutionException {

        final ResponseEntity<FabricApp[]> appsResponse = restTemplate.exchange("https://fabric.io/api/v2/apps?include=sdk_kits", HttpMethod.GET, new HttpEntity<>(httpHeaders), FabricApp[].class);
        if (appsResponse.getStatusCode() != HttpStatus.OK) {
            throw new FabricExecutionException("04 Expected HTTP OK but got " + appsResponse.getStatusCode());
        }
        return Arrays.asList(appsResponse.getBody());
    }

    private HttpHeaders logIn(String email, String password) throws FabricExecutionException {

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

        return httpHeaders;
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

    @Override
    public boolean isApplicable(Check check) {
        return check instanceof FabricCheck;
    }
}