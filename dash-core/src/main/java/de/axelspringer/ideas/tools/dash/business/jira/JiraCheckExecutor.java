package de.axelspringer.ideas.tools.dash.business.jira;

import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.check.CheckExecutor;
import de.axelspringer.ideas.tools.dash.business.check.CheckResult;
import de.axelspringer.ideas.tools.dash.business.jira.issuestatemapper.IssueStateMapper;
import de.axelspringer.ideas.tools.dash.business.jira.rest.Issue;
import de.axelspringer.ideas.tools.dash.presentation.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class JiraCheckExecutor implements CheckExecutor<JiraCheck> {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private JiraClient jiraClient;

    @Autowired
    private IssueStateMapper issueStateMapper;

    @Override
    public List<CheckResult> executeCheck(JiraCheck jiraCheck) {
        final List<Issue> issues = jiraClient.queryJiraForIssues(jiraCheck.getUrl(), jiraCheck.getJql(), jiraCheck.getUserName(), jiraCheck.getPassword());

        if (issues == null || issues.size() < 1) {
            final CheckResult checkResult = new CheckResult(State.GREEN, jiraCheck.getName(), "no issues", 1, 0, jiraCheck.getGroup())
                    .withLink(jiraCheck.getUrl()).withTeams(jiraCheck.getTeams());
            return Collections.singletonList(checkResult);
        }

        List<CheckResult> checkResults = new ArrayList<>();

        for (Issue issue : issues) {
            CheckResult checkResult = createCheckResultForIssue(jiraCheck, issue);
            checkResults.add(checkResult);
        }

        return checkResults;
    }

    CheckResult createCheckResultForIssue(JiraCheck jiraCheck, Issue issue) {
        final JiraProjectConfiguration jiraProjectConfiguration = jiraCheck.getJiraProjectConfiguration();

        final State state = issueStateMapper.mapToState(issue);
        final CheckResult checkResult = new CheckResult(state, jiraCheck.getName(), issue.getKey(), 1, state == State.GREEN ? 0 : 1, jiraCheck.getGroup())
                .withLink(jiraCheck.getUrl() + "/browse/" + issue.getKey()).withTeams(jiraCheck.getTeams());

        if (jiraProjectConfiguration.isIssueInProgress(issue)) {
            checkResult.markRunning();
        }

        final String assignee = issue.getFields().getAssignee() == null ? "nobody" : issue.getFields().getAssignee().getName();
        checkResult.withName(jiraCheck.getName() + " (" + assignee + ")");
        return checkResult;
    }

    @Override
    public boolean isApplicable(Check check) {
        return check instanceof JiraCheck;
    }
}
