package de.axelspringer.ideas.tools.dash.business.jira;

import de.axelspringer.ideas.tools.dash.business.jira.issuestatemapper.DefaultIssueStateMapper;
import de.axelspringer.ideas.tools.dash.business.jira.issuestatemapper.IssueStateMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JiraConfig {

    @ConditionalOnMissingBean
    @Bean
    public IssueStateMapper issueStateMapper() {
        return new DefaultIssueStateMapper();
    }
}
