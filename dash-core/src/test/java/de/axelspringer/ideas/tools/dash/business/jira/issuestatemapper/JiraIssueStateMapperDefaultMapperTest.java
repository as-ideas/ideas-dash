package de.axelspringer.ideas.tools.dash.business.jira.issuestatemapper;

import de.axelspringer.ideas.tools.dash.business.jira.JiraConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertTrue;

/**
 * Ensures that the DefaultJiraIssueStateMapper is used if no other one is configured
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = JiraIssueStateMapperDefaultMapperTest.IssueStateMapperDefaultMapperTestConfig.class)
public class JiraIssueStateMapperDefaultMapperTest {

    @Autowired
    private JiraIssueStateMapper jiraIssueStateMapper;

    @Test
    public void testCorrectMapperInjected() {

        assertTrue(jiraIssueStateMapper instanceof DefaultJiraIssueStateMapper);
    }

    @Configuration
    @Import(JiraConfig.class)
    static class IssueStateMapperDefaultMapperTestConfig {
    }
}
