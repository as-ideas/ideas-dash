package de.axelspringer.ideas.tools.dash.business.jira;

import com.google.gson.Gson;
import de.axelspringer.ideas.tools.dash.business.jira.rest.Issue;
import de.axelspringer.ideas.tools.dash.business.jira.rest.Project;
import de.axelspringer.ideas.tools.dash.business.jira.rest.SearchResult;
import de.axelspringer.ideas.tools.dash.util.CloseableHttpClientRestClient;
import de.axelspringer.ideas.tools.dash.util.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JiraClient {

    // as jira tends to behave badly sometimes (rejected requests, timeouts, ...) we need to retry to avoid wrong red bubbles
    private final static int RETRY_COUNT = 3;

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private RestClient restClient;

    @Autowired
    private Gson gson;

    public List<Issue> queryJiraForIssues(String jiraUrl, String jql, String username, String password) {

        // init request
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("maxResults", "30");
        requestParams.put("jql", jql);

        final String jiraApiUrl = jiraUrl + "/rest/api/2/search";

        // fetch results from jira
        log.debug("Retrieving jira status with JQL {}", jql);

        Exception lastException = null;

        for (int i = 0; i < RETRY_COUNT; i++) {

            try {
                return queryJiraForIssues(jiraApiUrl, requestParams, jql, username, password);
            } catch (Exception e) {
                lastException = e;
            }
        }

        log.error("error fetching jira results", lastException);
        throw new RuntimeException(lastException.getMessage(), lastException);
    }

    private List<Issue> queryJiraForIssues(String jiraApiUrl, Map<String, String> requestParams, String jql, String username, String password) throws Exception {

        final SearchResult searchResult;

        final String resultAsString = restClient.create()
                .withCredentials(username, password)
                .withQueryParameters(requestParams)
                .withTimeout(CloseableHttpClientRestClient.THIRTY_SECONS_IN_MS)
                .withHeader("accept-encoding", "gzip;q=0")
                .get(jiraApiUrl);

        searchResult = gson.fromJson(resultAsString, SearchResult.class);

        if (searchResult == null) {
            throw new Exception("deserialized to null. [Query=" + jql + "]");
        }

        return searchResult.getIssues();
    }

    public List<Project> fetchProjects(String jiraUrl, String username, String password) {

        final String resultAsString = restClient.create()
                .withCredentials(username, password)
                .withTimeout(CloseableHttpClientRestClient.FORTYFIVE_SECONS_IN_MS)
                .withHeader("accept-encoding", "gzip;q=0")
                .get(jiraUrl + "/rest/api/2/project");


        return Arrays.asList(gson.fromJson(resultAsString, Project[].class));
    }
}
