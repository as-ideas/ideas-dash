package de.axelspringer.ideas.tools.dash.business.jira;

import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.check.CheckExecutor;
import de.axelspringer.ideas.tools.dash.business.check.checkresult.CheckResult;
import de.axelspringer.ideas.tools.dash.business.jira.issuecheckresultdecorator.JiraIssueCheckResultDecorator;
import de.axelspringer.ideas.tools.dash.business.jira.issuestatemapper.JiraIssueStateMapper;
import de.axelspringer.ideas.tools.dash.business.jira.rest.Issue;
import de.axelspringer.ideas.tools.dash.presentation.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class JiraCheckExecutor implements CheckExecutor<JiraCheck> {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final JiraClient jiraClient;

    private final JiraIssueStateMapper jiraIssueStateMapper;

    private final JiraIssueCheckResultDecorator checkResultDecorator;

    @Autowired
    public JiraCheckExecutor(JiraClient jiraClient, JiraIssueStateMapper jiraIssueStateMapper, JiraIssueCheckResultDecorator checkResultDecorator) {
        this.jiraClient = jiraClient;
        this.jiraIssueStateMapper = jiraIssueStateMapper;
        this.checkResultDecorator = checkResultDecorator;
    }

    @Override
    public List<CheckResult> executeCheck(JiraCheck jiraCheck) {

        final List<Issue> issues = jiraClient.queryJiraForIssues(jiraCheck.getUrl(), jiraCheck.getJql(), jiraCheck.getUserName(), jiraCheck.getPassword());

        if (Optional.ofNullable(issues).map(List::isEmpty).orElse(true)) {
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

        final State state = jiraIssueStateMapper.mapToState(issue);
        final String name = checkResultDecorator.name(jiraCheck, issue);
        final String info = checkResultDecorator.info(issue);

        final CheckResult checkResult = new CheckResult(state, name, info, 1, state == State.GREEN ? 0 : 1, jiraCheck.getGroup())
                .withLink(jiraCheck.getUrl() + "/browse/" + issue.getKey()).withTeams(jiraCheck.getTeams());

        if (jiraProjectConfiguration.isIssueInProgress(issue)) {
            checkResult.markRunning();
        }

        return checkResult;
    }

    @Override
    public boolean isApplicable(Check check) {
        return check instanceof JiraCheck;
    }
}
