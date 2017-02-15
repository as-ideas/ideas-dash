package de.axelspringer.ideas.tools.dash.business.jira.issuecheckresultdecorator;

import de.axelspringer.ideas.tools.dash.business.jira.JiraCheck;
import de.axelspringer.ideas.tools.dash.business.jira.rest.Issue;

public class DefaultJiraIssueCheckResultDecorator implements JiraIssueCheckResultDecorator {

    @Override
    public String info(Issue issue) {
        return issue.getKey();
    }

    @Override
    public String name(JiraCheck jiraCheck, Issue issue) {

        final String assignee = issue.getFields().getAssignee() == null ? "nobody" : issue.getFields().getAssignee().getName();
        return jiraCheck.getName() + " (" + assignee + ")";
    }
}
