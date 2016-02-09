package de.axelspringer.ideas.tools.dash.business.jira;

import com.google.gson.Gson;
import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.check.CheckExecutor;
import de.axelspringer.ideas.tools.dash.business.check.CheckResult;
import de.axelspringer.ideas.tools.dash.business.jira.issuestatemapper.IssueStateMapper;
import de.axelspringer.ideas.tools.dash.business.jira.rest.Issue;
import de.axelspringer.ideas.tools.dash.presentation.State;
import de.axelspringer.ideas.tools.dash.util.CloseableHttpClientRestClient;
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

    @Autowired
    private IssueStateMapper issueStateMapper;

    @Override
    public List<CheckResult> executeCheck(JiraCheck jiraCheck) {
        final SearchResult searchResult = queryJira(jiraCheck);
        final List<Issue> issues = searchResult.getIssues();

        if (issues == null || issues.size() < 1) {
            final CheckResult checkResult = new CheckResult(State.GREEN, jiraCheck.getName(), "no issues", 1, 0, jiraCheck.getGroup())
                    .withLink(jiraCheck.getUrl()).withTeams(jiraCheck.getTeams());
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

        final State state = issueStateMapper.mapToState(issue);
        final CheckResult checkResult = new CheckResult(state, jiraCheck.getName(), issue.getKey(), 1, state == State.GREEN ? 0 : 1, jiraCheck.getGroup())
                .withLink(jiraCheck.getUrl() + "/browse/" + issue.getKey()).withTeams(jiraCheck.getTeams());

        if (jiraProjectConfiguration.isIssueInProgress(issue)) {
            checkResult.markRunning();
        }

        final String assignee = issue.getFields().getAssignee() == null ? "nobody" : issue.getFields().getAssignee().getName();
        checkResult.withName(jiraCheck.getName() + " (" + assignee + ")");
        return checkResult;
    }

    SearchResult queryJira(JiraCheck jiraCheck) {
        // init request
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("maxResults", "30");
        requestParams.put("jql", jiraCheck.getJql());

        final String jiraApiUrl = jiraCheck.getUrl() + "/rest/api/2/search";

        // fetch results from jira
        log.debug("Retrieving jira status with JQL {}", jiraCheck.getJql());
        final SearchResult searchResult;
        try {
            final String resultAsString = restClient.create()
                    .withCredentials(jiraCheck.getUserName(), jiraCheck.getPassword())
                    .withQueryParameters(requestParams)
                    .withTimeout(CloseableHttpClientRestClient.THIRTY_SECONS_IN_MS)
                    .withHeader("accept-encoding", "gzip;q=0")
                    .get(jiraApiUrl);
            searchResult = gson.fromJson(resultAsString, SearchResult.class);
        } catch (Exception e) {
            log.error("error fetching jira results", e);
            throw new RuntimeException(e.getMessage(), e);
        }

        if (searchResult == null) {
            log.error("deserialized to null. [Url= " + jiraApiUrl + ",Query=" + jiraCheck.getJql() + "]");
            throw new IllegalStateException("deserialized to null. [Query=" + jiraCheck.getJql() + "]");
        }

        return searchResult;
    }


    @Override
    public boolean isApplicable(Check check) {
        return check instanceof JiraCheck;
    }
}
