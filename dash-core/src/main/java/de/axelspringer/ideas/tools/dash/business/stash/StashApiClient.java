package de.axelspringer.ideas.tools.dash.business.stash;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.axelspringer.ideas.tools.dash.business.jira.JiraCheckExecutor;
import de.axelspringer.ideas.tools.dash.util.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//        > For those pull-ruquest which are open, how do I get the list of "APPROVED","UNAPPROVED" activities?
//    The following REST API will return you a list of all activities for a particular pull request:
//    GET /rest/api/1.0/projects/{projectKey}/repos/{repositorySlug}/pull-requests/{pullRequestId}/activities
@Service
public class StashApiClient {

    private static final Logger LOG = LoggerFactory.getLogger(JiraCheckExecutor.class);

    public static final String REST_API_REPOS_URL = "/rest/api/1.0/projects/PCP/repos?limit=100";
    public static final String REST_API_PULL_REQUESTS_PATTERN = "/rest/api/1.0/projects/PCP/repos/%s/pull-requests?state=OPEN";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestClient restClient;

    public List<StashRepo> readStashRepos(StashConfig stashConfig) {
        List<StashRepo> result = new ArrayList<>();

        try {
            final String reposResultAsString = getString(stashConfig, REST_API_REPOS_URL);

            final JsonNode reposJsonNode = objectMapper.readTree(reposResultAsString);
            for (JsonNode repo : reposJsonNode.get("values")) {
                final String repoName = repo.get("name").asText();
                result.add(new StashRepo(repoName));
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
        return result;
    }

    public List<StashPullRequest> readStashPullRequests(StashConfig stashConfig, StashRepo stashRepo) {
        List<StashPullRequest> result = new ArrayList<>();

        try {
            final String pullRequestResultAsString = getString(stashConfig, String.format(REST_API_PULL_REQUESTS_PATTERN, stashRepo.name()));

            final JsonNode pullRequestsJsonNode = objectMapper.readTree(pullRequestResultAsString);

            for (JsonNode pullRequestJsonNode : pullRequestsJsonNode.get("values")) {
                final StashPullRequest stashPullRequest = new StashPullRequest(pullRequestJsonNode.get("id").asText(), stashRepo);
                stashPullRequest.addCreatedDate(pullRequestJsonNode.get("createdDate").asLong());

                for (JsonNode jsonNode : pullRequestJsonNode.get("reviewers")) {
                    final JsonNode userNode = jsonNode.get("user");
                    stashPullRequest.addReviewer(new StashUser(userNode.get("name").asText(), userNode.get("displayName").asText()));
                }
                result.add(stashPullRequest);

            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
        return result;
    }

    String getString(StashConfig stashConfig, String urlPath) {
        return restClient.create().withCredentials(stashConfig.stashUserName(), stashConfig.stashUserPassword()).get(stashConfig.stashServerUrl() + urlPath);
    }
}