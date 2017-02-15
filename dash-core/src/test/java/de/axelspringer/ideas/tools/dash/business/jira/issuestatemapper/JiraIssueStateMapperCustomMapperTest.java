package de.axelspringer.ideas.tools.dash.business.jira.issuestatemapper;

import de.axelspringer.ideas.tools.dash.business.jira.JiraConfig;
import de.axelspringer.ideas.tools.dash.business.jira.rest.Issue;
import de.axelspringer.ideas.tools.dash.presentation.State;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertTrue;

/**
 * Ensures that the DefaultJiraIssueStateMapper is not used if another one is configured
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = JiraIssueStateMapperCustomMapperTest.IssueStateMapperCustomMapperTestConfig.class)
public class JiraIssueStateMapperCustomMapperTest {

    @Autowired
    private JiraIssueStateMapper jiraIssueStateMapper;

    @Test
    public void testCorrectMapperInjected() {

        assertTrue(jiraIssueStateMapper instanceof CustomJiraIssueStateMapper);
    }

    static class CustomJiraIssueStateMapper implements JiraIssueStateMapper {

        @Override
        public State mapToState(Issue issue) {
            return State.GREEN;
        }
    }

    @Configuration
    @Import(JiraConfig.class)
    static class IssueStateMapperCustomMapperTestConfig {

        @Bean
        public JiraIssueStateMapper issueStateMapper() {
            return new CustomJiraIssueStateMapper();
        }
    }
}
