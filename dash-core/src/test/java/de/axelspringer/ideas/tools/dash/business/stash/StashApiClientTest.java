package de.axelspringer.ideas.tools.dash.business.stash;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class StashApiClientTest {

    private static final StashConfig GIVEN_STASH_CONFIG = new StashConfig("http://stash.rockt", "", "");

    private static final String GIVEN_REPO_NAME = "pcp-sso";

    @Spy
    @InjectMocks
    private StashApiClient stashApiClient;

    @Spy
    private ObjectMapper objectMapper;

    @Test
    public void readStashRepos() throws Exception {
        String theString = IOUtils.toString(getClass().getResourceAsStream("/stash-repos-json-answer.json"), "UTF-8");
        doReturn(theString).when(stashApiClient).getString(any(StashConfig.class), anyString());

        final List<StashRepo> stashRepos = stashApiClient.readStashRepos(GIVEN_STASH_CONFIG);

        assertEquals(stashRepos.size(), 25);
        assertEquals(stashRepos.get(0).name(), "c1-ocb-integrator");
    }

    @Test
    public void readStashPullRequests() throws Exception {
        String theString = IOUtils.toString(getClass().getResourceAsStream("/stash-pull-requests-json-answer.json"), "UTF-8");
        doReturn(theString).when(stashApiClient).getString(any(StashConfig.class), anyString());

        final List<StashPullRequest> stashPullRequests = stashApiClient.readStashPullRequests(GIVEN_STASH_CONFIG, new StashRepo(GIVEN_REPO_NAME));

        assertEquals(stashPullRequests.size(), 1);
        assertEquals(stashPullRequests.get(0).repo().name(), GIVEN_REPO_NAME);
        assertEquals(stashPullRequests.get(0).reviewers().size(), 1);
        assertEquals(stashPullRequests.get(0).reviewers().get(0).name(), "sbieler1");
        assertEquals(stashPullRequests.get(0).reviewers().get(0).nameDisplayed(), "Stefan Bieler");
        assertEquals(stashPullRequests.get(0).id(), "65");

    }


}