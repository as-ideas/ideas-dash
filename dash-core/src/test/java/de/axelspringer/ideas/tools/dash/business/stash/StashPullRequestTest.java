package de.axelspringer.ideas.tools.dash.business.stash;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

public class StashPullRequestTest {

    @Test
    public void givenNoCreateDateTimestamp_shouldHaveNoAge() throws Exception {
        StashPullRequest stashPullRequest = new StashPullRequest("some-in", new StashRepo("some-repo"));
        stashPullRequest.addCreatedDate(null);

        assertThat(stashPullRequest.getAgeInDays(), nullValue());
    }

    @Test
    public void givenCreateDateTimestamp_shouldHaveCorrectAge() throws Exception {
        StashPullRequest stashPullRequest = new StashPullRequest("some-in", new StashRepo("some-repo"));
        stashPullRequest.addCreatedDate(1441204503000L);

        assertThat(stashPullRequest.getAgeInDays(), equalTo(407L));
    }
}