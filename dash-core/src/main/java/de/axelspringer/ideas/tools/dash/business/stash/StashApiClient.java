package de.axelspringer.ideas.tools.dash.business.stash;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.axelspringer.ideas.tools.dash.util.RestClient;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//        > For those pull-ruquest which are open, how do I get the list of "APPROVED","UNAPPROVED" activities?
//    The following REST API will return you a list of all activities for a particular pull request:
//    GET /rest/api/1.0/projects/{projectKey}/repos/{repositorySlug}/pull-requests/{pullRequestId}/activities
@Service
public class StashApiClient {

    @Autowired
    private StashConfig stashConfig;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestClient restClient;

    @SneakyThrows
    public List<StashRepo> fetchStashRepos() {
        final List<StashRepo> result = new ArrayList<>();

        final String responseString = httpRequest(stashConfig.getRepoUrl());
        final JsonNode reposJsonNode = objectMapper.readTree(responseString);
        for (JsonNode repo : reposJsonNode.get("values")) {
            final String repoName = repo.get("name").asText();
            result.add(new StashRepo(repoName));
        }

        return result;
    }

    @SneakyThrows
    public List<StashPullRequest> fetchPullRequestsOfRepo(StashRepo stashRepo) {
        final List<StashPullRequest> result = new ArrayList<>();

        final String responseString = httpRequest(stashConfig.pullRequestUrl(stashRepo.getName()));
        final JsonNode pullRequestsJsonNode = objectMapper.readTree(responseString);

        for (JsonNode pullRequestJsonNode : pullRequestsJsonNode.get("values")) {
            final StashPullRequest stashPullRequest = new StashPullRequest(pullRequestJsonNode.get("id").asText(), stashRepo);
            stashPullRequest.addCreatedDate(pullRequestJsonNode.get("createdDate").asLong());

            for (JsonNode jsonNode : pullRequestJsonNode.get("reviewers")) {
                final JsonNode userNode = jsonNode.get("user");
                stashPullRequest.addReviewer(new StashUser(userNode.get("name").asText(), userNode.get("displayName").asText()));
            }
            result.add(stashPullRequest);
        }

        return result;
    }

    String httpRequest(String url) {
        return restClient.create().withCredentials(stashConfig.getUsername(), stashConfig.getPassword()).get(url);
    }
}