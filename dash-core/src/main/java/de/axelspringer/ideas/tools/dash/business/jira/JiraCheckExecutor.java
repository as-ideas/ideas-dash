package de.axelspringer.ideas.tools.dash.business.jira;

import com.google.gson.Gson;
import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.check.CheckExecutor;
import de.axelspringer.ideas.tools.dash.business.check.CheckResult;
import de.axelspringer.ideas.tools.dash.business.jira.rest.Issue;
import de.axelspringer.ideas.tools.dash.presentation.State;
import de.axelspringer.ideas.tools.dash.util.RestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class JiraCheckExecutor implements CheckExecutor<JiraCheck> {

    @Autowired
    private RestClient restClient;

    @Autowired
    private Gson gson;

    @Override
    public List<CheckResult> executeCheck(JiraCheck jiraCheck) {

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
            return Collections.singletonList(new CheckResult(State.RED, "error", jiraCheck.getJql(), 0, 0, jiraCheck.getGroup()).withTeam(jiraCheck.getTeam()));
        }
        //gson may not parse the response correctly
        if (searchResult == null) {
            log.error("deserialized to null. [Query=" + jiraCheck.getJql() + "]");
            return Collections.singletonList(new CheckResult(State.RED, "error 2", jiraCheck.getJql(), 0, 0, jiraCheck.getGroup()).withTeam(jiraCheck.getTeam()));
        }

        final List<Issue> issues = searchResult.getIssues();

        if (issues == null || issues.size() < 1) {
            final CheckResult checkResult = new CheckResult(State.GREEN, jiraCheck.getName(), "no issues", 1, 0, jiraCheck.getGroup())
                    .withLink(jiraCheck.getUrl()).withTeam(jiraCheck.getTeam());
            return Collections.singletonList(checkResult);
        }

        List<CheckResult> checkResults = new ArrayList<>();
        final JiraProjectConfiguration jiraProjectConfiguration = jiraCheck.getJiraProjectConfiguration();

        for (Issue issue : issues) {

            final State staticState = jiraProjectConfiguration.stateForIssueState(issue.getFields().getStatus().getName());
            final State state = staticState != null ? staticState : state(issue);
            final CheckResult checkResult = new CheckResult(state, jiraCheck.getName(), issue.getKey(), 1, 1, jiraCheck.getGroup())
                    .withLink(jiraCheck.getUrl() + "/browse/" + issue.getKey()).withTeam(jiraCheck.getTeam());

            if (jiraProjectConfiguration.isIssueInProgress(issue)) {
                checkResult.markRunning();
            }

            checkResult.withName(jiraCheck.getName() + " (" + issue.getFields().getAssignee().getName() + ")");

            checkResults.add(checkResult);
        }

        return checkResults;
    }


    private State state(Issue issue) {

        // Bugs
        if (issue.isBug()) {
            // treat null-priority as blocker
            String priority = issue.getFields().getPriority() != null ? issue.getFields().getPriority().getName() : Priority.BLOCKER_NAME;
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
