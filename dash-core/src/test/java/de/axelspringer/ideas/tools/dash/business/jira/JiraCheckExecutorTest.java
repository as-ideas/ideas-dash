package de.axelspringer.ideas.tools.dash.business.jira;

import de.axelspringer.ideas.tools.dash.business.check.checkresult.CheckResult;
import de.axelspringer.ideas.tools.dash.business.jira.issuecheckresultdecorator.DefaultJiraIssueCheckResultDecorator;
import de.axelspringer.ideas.tools.dash.business.jira.issuestatemapper.DefaultJiraIssueStateMapper;
import de.axelspringer.ideas.tools.dash.business.jira.rest.*;
import de.axelspringer.ideas.tools.dash.presentation.State;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class JiraCheckExecutorTest {

    private JiraClient jiraClient = mock(JiraClient.class);

    private JiraCheckExecutor jiraCheckExecutor = new JiraCheckExecutor(jiraClient, new DefaultJiraIssueStateMapper(), new DefaultJiraIssueCheckResultDecorator());

    @Test
    public void testNoIssuesFound() throws Exception {

        doReturn(null).when(jiraClient).queryJiraForIssues(anyString(), anyString(), anyString(), anyString());

        List<CheckResult> checkResults = jiraCheckExecutor.executeCheck(jiraCheck("FOO"));

        assertThat(checkResults.size(), is(1));
        CheckResult singleResult = checkResults.get(0);
        assertThat(singleResult.getState(), is(State.GREEN));
        assertNull(singleResult.getGroup());
        assertThat(singleResult.getInfo(), is(equalTo("no issues")));
        assertThat(singleResult.getFailCount(), is(0));
        assertThat(singleResult.getTestCount(), is(1));
    }

    @Test
    public void testTwoIssuesFound() throws Exception {

        final JiraCheck jiraCheck = jiraCheck("FOO");

        final List<Issue> jiraIssues = new ArrayList<>();
        jiraIssues.add(jiraIssue("TEST-123", "John Doe"));
        jiraIssues.add(jiraIssue("TEST-007", "James Bond"));
        doReturn(jiraIssues).when(jiraClient).queryJiraForIssues(anyString(), anyString(), anyString(), anyString());

        final List<CheckResult> checkResults = jiraCheckExecutor.executeCheck(jiraCheck);

        assertThat(checkResults.size(), is(2));
    }

    @Test
    public void testCheckResultDecoration() {

        final String name = "FOO";
        final String assigneeName = "John Doe";
        final String issueKey = "TEST-123";

        // configure jira-mock
        final Issue jiraIssue = jiraIssue(issueKey, assigneeName);
        doReturn(Collections.singletonList(jiraIssue)).when(jiraClient).queryJiraForIssues(anyString(), anyString(), anyString(), anyString());

        // initialize jira check
        final JiraCheck jiraCheck = jiraCheck(name);

        // execute/assert
        final List<CheckResult> checkResults = jiraCheckExecutor.executeCheck(jiraCheck);

        assertEquals(1, checkResults.size());
        final CheckResult result = checkResults.get(0);
        assertEquals(name + " (" + assigneeName + ")", result.getName());
        assertEquals(issueKey, result.getInfo());
    }

    final JiraCheck jiraCheck(String name) {
        return new JiraCheck(name, null, "http:foo", "foouser", "fooopass", "foojql", null);
    }

    final Issue jiraIssue(String issueKey, String assigneeName) {
        final Issue jiraIssue = new Issue();
        jiraIssue.setKey(issueKey);
        final Fields fields = new Fields();
        final Assignee assignee = new Assignee();
        assignee.setName(assigneeName);
        final IssueType issueType = new IssueType();
        issueType.setName("foo");
        final IssueStatus status = new IssueStatus();
        status.setName("bar");
        fields.setStatus(status);
        fields.setIssuetype(issueType);
        fields.setAssignee(assignee);
        jiraIssue.setFields(fields);

        return jiraIssue;
    }
}