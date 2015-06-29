package de.axelspringer.ideas.tools.dash.business.jira;

import com.google.gson.Gson;
import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.check.CheckExecutor;
import de.axelspringer.ideas.tools.dash.business.check.CheckResult;
import de.axelspringer.ideas.tools.dash.business.jira.rest.Issue;
import de.axelspringer.ideas.tools.dash.presentation.State;
import de.axelspringer.ideas.tools.dash.util.RestClient;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JiraCheckExecutor implements CheckExecutor<JiraCheck> {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(JiraCheckExecutor.class);

    @Autowired
    private RestClient restClient;

    @Autowired
    private Gson gson;

    @Override
    public List<CheckResult> executeCheck(JiraCheck jiraCheck) {
        final SearchResult searchResult = queryJira(jiraCheck);
        final List<Issue> issues = searchResult.getIssues();

        if (issues == null || issues.size() < 1) {
            final CheckResult checkResult = new CheckResult(State.GREEN, jiraCheck.getName(), "no issues", 1, 0, jiraCheck.getGroup())
                    .withLink(jiraCheck.getUrl()).withTeam(jiraCheck.getTeam());
            return Collections.singletonList(checkResult);
        }

        List<CheckResult> checkResults = new ArrayList<>();

        for (Issue issue : issues) {
            CheckResult checkResult = createCheckResultForIssue(jiraCheck, issue);
            checkResults.add(checkResult);
        }

        return checkResults;
    }

    CheckResult createCheckResultForIssue(JiraCheck jiraCheck, Issue issue) {
        final JiraProjectConfiguration jiraProjectConfiguration = jiraCheck.getJiraProjectConfiguration();

        final State staticState = jiraProjectConfiguration.stateForIssueState(issue.getFields().getStatus().getName());
        final State state = staticState != null ? staticState : state(issue);
        final CheckResult checkResult = new CheckResult(state, jiraCheck.getName(), issue.getKey(), 1, state == State.GREEN ? 0 : 1, jiraCheck.getGroup())
                .withLink(jiraCheck.getUrl() + "/browse/" + issue.getKey()).withTeam(jiraCheck.getTeam());

        if (jiraProjectConfiguration.isIssueInProgress(issue)) {
            checkResult.markRunning();
        }

        checkResult.withName(jiraCheck.getName() + " (" + issue.getFields().getAssignee().getName() + ")");
        return checkResult;
    }

    SearchResult queryJira(JiraCheck jiraCheck) {
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
            throw new RuntimeException(e.getMessage(), e);
        }

        if (searchResult == null) {
            log.error("deserialized to null. [Query=" + jiraCheck.getJql() + "]");
            throw new IllegalStateException("deserialized to null. [Query=" + jiraCheck.getJql() + "]");
        }

        return searchResult;
    }

    private State state(Issue issue) {
        if (issue.isBug()) {
            return stateForBug(issue);
        }
        return stateForIssue(issue);
    }

    private State stateForIssue(Issue issue) {
        switch (issue.getFields().getStatus().getName().toLowerCase()) {
            case "done":
                return State.GREEN;
            case "in progress":
                return State.GREY;
            default:
                return State.YELLOW;
        }
    }

    private State stateForBug(Issue issue) {
        if (isPriorityBlocker(issue)) {
            return State.RED;
        }

        if (isStatusDone(issue)) {
            return State.GREEN;
        }
        return State.YELLOW;
    }

    private boolean isStatusDone(Issue issue) {
        return issue.getFields().getStatus().getName().toLowerCase().equals("done");
    }

    private boolean isPriorityBlocker(Issue issue) {
        Priority jiraTicketPriority = issue.getFields().getPriority();
        if (jiraTicketPriority == null) {
            log.error("Priority is not set! Issue: " + issue.getKey());
            return false;
        }


        String priority = jiraTicketPriority.getName();
        return Priority.BLOCKER_NAME.equals(priority);
    }

    @Override
    public boolean isApplicable(Check check) {
        return check instanceof JiraCheck;
    }
}
