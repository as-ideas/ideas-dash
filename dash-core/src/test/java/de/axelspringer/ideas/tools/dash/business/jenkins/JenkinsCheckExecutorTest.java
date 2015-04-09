package de.axelspringer.ideas.tools.dash.business.jenkins;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JenkinsCheckExecutorTest {

    private final JenkinsCheckExecutor jenkinsCheckExecutor = new JenkinsCheckExecutor();

    @Test
    public void testGetShortNameWithValidName() {

        final String name = "DEV_AppC_AppConnect";

        final String shortName = jenkinsCheckExecutor.shortName(name);

        assertEquals("AppC", shortName);
    }

    @Test
    public void testGetShortNameWithInValidName() {

        final String name = "SomeJobName";

        final String shortName = jenkinsCheckExecutor.shortName(name);

        assertEquals(name, shortName);
    }
}