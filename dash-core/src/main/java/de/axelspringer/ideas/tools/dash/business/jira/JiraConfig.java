package de.axelspringer.ideas.tools.dash.business.jira;

import de.axelspringer.ideas.tools.dash.business.jira.issuecheckresultdecorator.DefaultJiraIssueCheckResultDecorator;
import de.axelspringer.ideas.tools.dash.business.jira.issuecheckresultdecorator.JiraIssueCheckResultDecorator;
import de.axelspringer.ideas.tools.dash.business.jira.issuestatemapper.DefaultJiraIssueStateMapper;
import de.axelspringer.ideas.tools.dash.business.jira.issuestatemapper.JiraIssueStateMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JiraConfig {

    @ConditionalOnMissingBean
    @Bean
    public JiraIssueStateMapper issueStateMapper() {
        return new DefaultJiraIssueStateMapper();
    }

    @ConditionalOnMissingBean
    @Bean
    public JiraIssueCheckResultDecorator checkResultDecorator() {
        return new DefaultJiraIssueCheckResultDecorator();
    }
}
