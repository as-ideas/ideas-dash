package de.axelspringer.ideas.tools.dash.business.jira;

import de.axelspringer.ideas.tools.dash.business.jira.rest.Fields;
import de.axelspringer.ideas.tools.dash.business.jira.rest.Issue;
import de.axelspringer.ideas.tools.dash.business.jira.rest.IssueStatus;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class JiraProjectConfigurationTest {

    private JiraProjectConfiguration jiraProjectConfiguration;

    @Before
    public void beforeMethod() throws Exception {
        jiraProjectConfiguration = new JiraProjectConfiguration();
    }

    @Test
    public void testIsIssueInProgress() throws Exception {
        Issue issue = givenIssueWithStatus("ASDF");
        assertThat(jiraProjectConfiguration.isIssueInProgress(issue), is(false));

        jiraProjectConfiguration.addIssueStateInProgress("ASDF");
        assertThat(jiraProjectConfiguration.isIssueInProgress(issue), is(true));
    }

    private Issue givenIssueWithStatus(String isSt) {
        Issue issue = new Issue();
        Fields fields = new Fields();
        IssueStatus issueStatus = new IssueStatus();
        issueStatus.setName(isSt);
        fields.setStatus(issueStatus);
        issue.setFields(fields);
        return issue;
    }


}