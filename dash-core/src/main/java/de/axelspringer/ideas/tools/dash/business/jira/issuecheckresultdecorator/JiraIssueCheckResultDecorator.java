package de.axelspringer.ideas.tools.dash.business.jira.issuecheckresultdecorator;

import de.axelspringer.ideas.tools.dash.business.check.checkresult.CheckResult;
import de.axelspringer.ideas.tools.dash.business.jira.JiraCheck;
import de.axelspringer.ideas.tools.dash.business.jira.rest.Issue;

/**
 * Decorates the CheckResult for the UI based on the check configuration and corresponding Jira issue
 */
public interface JiraIssueCheckResultDecorator {

    String info(Issue issue);

    String name(JiraCheck jiraCheck, Issue issue);

    default CheckResult decorate(CheckResult checkResult, JiraCheck jiraCheck, Issue issue) {
        return checkResult;
    };
}
