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
 * Ensures that the DefaultIssueStateMapper is used if no other one is configured
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = IssueStateMapperDefaultMapperTest.IssueStateMapperDefaultMapperTestConfig.class)
public class IssueStateMapperDefaultMapperTest {

    @Autowired
    private IssueStateMapper issueStateMapper;

    @Test
    public void testCorrectMapperInjected() {

        assertTrue(issueStateMapper instanceof DefaultIssueStateMapper);
    }

    @Configuration
    @Import(JiraConfig.class)
    static class IssueStateMapperDefaultMapperTestConfig {
    }

    // TODO: you should add some more tests for the behaviour. but you also can do this using mockito/dont need the spring runner
}
