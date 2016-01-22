package de.axelspringer.ideas.tools.dash.business.stash;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import de.axelspringer.ideas.tools.dash.TestTeam;
import de.axelspringer.ideas.tools.dash.business.check.CheckResult;
import de.axelspringer.ideas.tools.dash.business.failure.FailureGroup;
import de.axelspringer.ideas.tools.dash.presentation.State;

@RunWith(MockitoJUnitRunner.class)
public class StashCheckExecutorTest {

	private static final StashConfig GIVEN_STASH_CONFIG = new StashConfig("http://stash.rockt", "", "");

	@InjectMocks
	private StashCheckExecutor stashCheckExecutor;

	@Mock
	private StashApiClient stashApiClient;

	private List<CheckResult> checkResult;

	@Before
	public void beforeMethod() throws Exception {
		checkResult = null;
	}

	@Test
	public void execute_check_with_two_repos_and_three_merge_requests_results_in_three_checks() throws Exception {
		givenTwoReposAndThreeMergeRequests();

		whenCheckIsExecuted();

		thenResultHasThreeCheckResults();
	}

	@Test
	public void execute_check_whose_result_is_related_to_two_teams() throws Exception {
		givenOneReposAndOneMergeRequest();

		whenCheckIsExecutedWithTwoTeams();

		assertEquals(2, checkResult.get(0).getTeams().size());
		assertEquals(TestTeam.INSTANCE, this.checkResult.get(0).getTeams().get(0));
		assertEquals(TestTeam.INSTANCE, this.checkResult.get(0).getTeams().get(1));
	}

	private void givenTwoReposAndThreeMergeRequests() {

		List<StashRepo> stashRepos = createRepositories(2);

		mockStashapiClientReadStashRepos(stashRepos);
		
		mockStashApiClientReadStashPullRequest(stashRepos.get(0),
				asList(new StashPullRequest("1", stashRepos.get(0)), new StashPullRequest("2", stashRepos.get(0))));
		mockStashApiClientReadStashPullRequest(stashRepos.get(1), asList(new StashPullRequest("3", stashRepos.get(1))));
	}

	private void givenOneReposAndOneMergeRequest() {
		List<StashRepo> stashRepos = createRepositories(1);

		mockStashapiClientReadStashRepos(stashRepos);
		
		mockStashApiClientReadStashPullRequest(stashRepos.get(0), asList(new StashPullRequest("1", stashRepos.get(0))));
	}

	private void mockStashApiClientReadStashPullRequest(StashRepo stashRepo, List<StashPullRequest> stashPullRequests) {
		doReturn(stashPullRequests).when(stashApiClient).readStashPullRequests(GIVEN_STASH_CONFIG, stashRepo);
	}

	private void mockStashapiClientReadStashRepos(List<StashRepo> stashRepos) {
		doReturn(stashRepos).when(stashApiClient).readStashRepos(any(StashConfig.class));
	}

	private List<StashRepo> createRepositories(int numberOfRepositories) {
		List<StashRepo> stashRepos = new ArrayList<>();
		for (int i = 1; i <= numberOfRepositories; i++) {
			stashRepos.add(new StashRepo("repo" + i));
		}
		return stashRepos;
	}

	private void whenCheckIsExecuted() {
		checkResult = stashCheckExecutor.executeCheck(
				new StashCheck("myCheck", FailureGroup.INSTANCE, Arrays.asList(TestTeam.INSTANCE), GIVEN_STASH_CONFIG));
	}

	private void whenCheckIsExecutedWithTwoTeams() {
		checkResult = stashCheckExecutor.executeCheck(new StashCheck("myCheck", FailureGroup.INSTANCE,
				Arrays.asList(TestTeam.INSTANCE, TestTeam.INSTANCE), GIVEN_STASH_CONFIG));
	}

	private void thenResultHasThreeCheckResults() {
		assertEquals(3, checkResult.size());

		final CheckResult checkResult1 = this.checkResult.get(0);
		assertEquals("Merge Request repo1", checkResult1.getName());
		assertEquals("[]", checkResult1.getInfo());
		assertEquals("http://stash.rockt/projects/PCP/repos/repo1/pull-requests/1", checkResult1.getLink());
		assertEquals(FailureGroup.INSTANCE, checkResult1.getGroup());
		assertEquals(State.RED, checkResult1.getState());
		assertEquals(TestTeam.INSTANCE, checkResult1.getTeams().get(0));

		final CheckResult checkResult2 = this.checkResult.get(1);
		assertEquals("Merge Request repo1", checkResult2.getName());
		assertEquals("[]", checkResult2.getInfo());
		assertEquals("http://stash.rockt/projects/PCP/repos/repo1/pull-requests/2", checkResult2.getLink());
		assertEquals(FailureGroup.INSTANCE, checkResult2.getGroup());
		assertEquals(State.RED, checkResult2.getState());
		assertEquals(TestTeam.INSTANCE, checkResult2.getTeams().get(0));

		final CheckResult checkResult3 = this.checkResult.get(2);
		assertEquals("Merge Request repo2", checkResult3.getName());
		assertEquals("[]", checkResult3.getInfo());
		assertEquals("http://stash.rockt/projects/PCP/repos/repo2/pull-requests/3", checkResult3.getLink());
		assertEquals(FailureGroup.INSTANCE, checkResult3.getGroup());
		assertEquals(State.RED, checkResult3.getState());
		assertEquals(TestTeam.INSTANCE, checkResult3.getTeams().get(0));
	}
}