package de.axelspringer.ideas.tools.dash.business.jira;

import com.google.gson.Gson;
import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.check.CheckExecutor;
import de.axelspringer.ideas.tools.dash.business.check.CheckResult;
import de.axelspringer.ideas.tools.dash.presentation.State;
import de.axelspringer.ideas.tools.dash.util.RestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class JiraCheckExecutor implements CheckExecutor {

    @Autowired
    private RestClient restClient;

    @Autowired
    private Gson gson;

    @Override
    public List<CheckResult> executeCheck(Check check) {

        JiraCheck jiraCheck = (JiraCheck) check;

        // init request
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("maxResults", "30");
        requestParams.put("jql", jiraCheck.getJql());

        // fetch results from jira
        log.debug("Retrieving jira status with JQL {}", jiraCheck.getJql());
        final SearchResult searchResult;
        try {
            final String resultAsString = restClient.get(jiraCheck.getUrl() + "/rest/api/2/search", jiraCheck.getUserName(), jiraCheck.getPassword(), requestParams);
            searchResult = gson.fromJson(resultAsString, SearchResult.class);
        } catch (Exception e) {
            log.error("error fetching jira results", e);
            return Arrays.asList(new CheckResult(State.RED, "error", jiraCheck.getJql(), 0, 0, jiraCheck.getStage()).withTeam(jiraCheck.getTeam()));
        }
        //gson may not parse the response correctly
        if (searchResult == null) {
            log.error("deserialized to null. [Query=" + jiraCheck.getJql() + "]");
            return Arrays.asList(new CheckResult(State.RED, "error 2", jiraCheck.getJql(), 0, 0, jiraCheck.getStage()).withTeam(jiraCheck.getTeam()));
        }

        final List<Issue> issues = searchResult.getIssues();

        if (issues == null || issues.size() < 1) {
            final CheckResult checkResult = new CheckResult(State.GREEN, jiraCheck.getName(), "no issues", 1, 0, jiraCheck.getStage())
                    .withLink(jiraCheck.getUrl()).withTeam(jiraCheck.getTeam());
            return Collections.singletonList(checkResult);
        }

        List<CheckResult> checkResults = new ArrayList<>();
        for (Issue issue : issues) {
            final State state = state(issue);
            final CheckResult checkResult = new CheckResult(state, jiraCheck.getName(), issue.getKey(), 1, 1, jiraCheck.getStage())
                    .withLink(jiraCheck.getUrl() + "/browse/" + issue.getKey()).withTeam(jiraCheck.getTeam());
            checkResults.add(checkResult);
        }

        return checkResults;
    }

    private State state(Issue issue) {

        // Bugs
        if (issue.isBug()) {
            String priority = issue.getFields().getPriority().getName();
            if (Priority.BLOCKER_NAME.equals(priority)) {
                return State.RED;
            }
            if (issue.getFields().getStatus().getName().toLowerCase().equals("done")) {
                return State.GREEN;
            }
            return State.YELLOW;
        }

        // Issues
        switch (issue.getFields().getStatus().getName().toLowerCase()) {
            case "done":
                return State.GREEN;
            case "in progress":
                return State.GREY;
            default:
                return State.YELLOW;
        }
    }

    @Override
    public boolean isApplicable(Check check) {
        return check instanceof JiraCheck;
    }
}
