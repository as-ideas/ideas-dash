package de.axelspringer.ideas.tools.dash.business.jira;

import de.axelspringer.ideas.tools.dash.business.check.checkresult.CheckResult;
import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.business.customization.Team;
import de.axelspringer.ideas.tools.dash.business.jira.rest.Issue;
import de.axelspringer.ideas.tools.dash.business.jira.rest.SearchResult;
import de.axelspringer.ideas.tools.dash.presentation.State;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class JiraCheckExecutorTest {

    public static final String GIVEN_NAME = "name";
    public static final Team GIVEN_TEAM = new Team() {
        @Override
        public String getTeamName() {
            return "given team";
        }

        @Override
        public String getJiraTeamName() {
            return "GIVT";
        }
    };
    public static final String GIVEN_URL = "url";
    public static final String GIVEN_USER = "user";
    public static final String GIVEN_PASSWORT = "pw";
    public static final String GIVEN_JQL = "jql";
    public static final Group GIVEN_GROUP = mock(Group.class);

    @Mock
    private JiraClient jiraClient;

    @Spy
    @InjectMocks
    private JiraCheckExecutor jiraCheckExecutor;

    @Test
    public void executeChecks_WithNoIssuesFound() throws Exception {
        doReturn(new SearchResult().getIssues()).when(jiraClient).queryJiraForIssues(anyString(), anyString(), anyString(), anyString());

        List<CheckResult> checkResults = jiraCheckExecutor.executeCheck(new JiraCheck(GIVEN_NAME, Collections.singletonList(GIVEN_TEAM), GIVEN_URL, GIVEN_USER, GIVEN_PASSWORT, GIVEN_JQL, GIVEN_GROUP));

        assertThat(checkResults.size(), is(1));
        CheckResult singleResult = checkResults.get(0);
        assertThat(singleResult.getState(), is(State.GREEN));
        assertThat(singleResult.getGroup(), is(GIVEN_GROUP));
        assertThat(singleResult.getInfo(), is(equalTo("no issues")));
        assertThat(singleResult.getLink(), is(GIVEN_URL));
        assertThat(singleResult.getName(), is(GIVEN_NAME));
        assertThat(singleResult.getTeams().get(0), is(GIVEN_TEAM.getTeamName()));
        assertThat(singleResult.getFailCount(), is(0));
        assertThat(singleResult.getTestCount(), is(1));
    }

    @Test
    public void executeChecks_IssuesFound() throws Exception {
        givenTwoIssuesAreFound();
        givenDoNothingForFoundIssues();

        List<CheckResult> checkResults = jiraCheckExecutor.executeCheck(new JiraCheck(GIVEN_NAME, Collections.singletonList(GIVEN_TEAM), GIVEN_URL, GIVEN_USER, GIVEN_PASSWORT, GIVEN_JQL, GIVEN_GROUP));

        assertThat(checkResults.size(), is(2));
    }

    private void givenTwoIssuesAreFound() {
        SearchResult toBeReturned = new SearchResult();
        toBeReturned.setIssues(Arrays.asList(new Issue(), new Issue()));
        doReturn(toBeReturned.getIssues()).when(jiraClient).queryJiraForIssues(anyString(), anyString(), anyString(), anyString());
    }

    private void givenDoNothingForFoundIssues() {
        doReturn(null).when(jiraCheckExecutor).createCheckResultForIssue(any(JiraCheck.class), any(Issue.class));
    }
}