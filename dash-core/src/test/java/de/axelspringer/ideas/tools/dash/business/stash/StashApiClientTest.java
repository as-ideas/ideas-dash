package de.axelspringer.ideas.tools.dash.business.stash;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.axelspringer.ideas.tools.dash.util.CloseableHttpClientRestClient;
import de.axelspringer.ideas.tools.dash.util.RestClient;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StashApiClientTest {

    @InjectMocks
    private StashApiClient stashApiClient;

    @Mock
    private RestClient restClient;
    @Mock
    private CloseableHttpClientRestClient closeableHttpClientRestClient;

    @Before
    public void setup() throws IOException {
        Whitebox.setInternalState(stashApiClient, "objectMapper", new ObjectMapper());
        Whitebox.setInternalState(stashApiClient, "stashConfig", new StashConfig(true, "repo", "username", "password"));

        final String reposResponseString = IOUtils.toString(getClass().getResourceAsStream("/stash-repos-json-answer.json"), "UTF-8");
        final String pullRequestResponseString = IOUtils.toString(getClass().getResourceAsStream("/stash-pull-requests-json-answer.json"), "UTF-8");

        when(restClient.create()).thenReturn(closeableHttpClientRestClient);
        when(closeableHttpClientRestClient.withCredentials(anyString(), anyString())).thenReturn(closeableHttpClientRestClient);
        when(closeableHttpClientRestClient.get(eq("repo"))).thenReturn(reposResponseString);
        when(closeableHttpClientRestClient.get(matches(".*pull-requests.*"))).thenReturn(pullRequestResponseString);
    }

    @Test
    public void fetchPullRequestsOfRepo_should_return_repos() {
        final List<StashRepo> stashRepos = stashApiClient.fetchStashRepos();

        assertThat(stashRepos, hasSize(1));
        assertThat(stashRepos.get(0), hasProperty("name", equalTo("c1-ocb-integrator")));
    }

    @Test
    public void fetchPullRequestsOfRepo_should_return_pull_request() {
        final List<StashPullRequest> stashPullRequests = stashApiClient.fetchPullRequestsOfRepo(new StashRepo("pcp-sso"));

        assertThat(stashPullRequests, hasSize(1));
        assertThat(stashPullRequests.get(0).repo().getName(), equalTo("pcp-sso"));
        assertThat(stashPullRequests.get(0).id(), equalTo("65"));

        assertThat(stashPullRequests.get(0).reviewers(), hasSize(1));
        assertThat(stashPullRequests.get(0).reviewers().get(0).name(), equalTo("sX"));
        assertThat(stashPullRequests.get(0).reviewers().get(0).nameDisplayed(), equalTo("XXX XXX"));
    }
}