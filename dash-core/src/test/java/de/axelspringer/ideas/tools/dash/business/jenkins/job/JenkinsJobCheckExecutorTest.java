package de.axelspringer.ideas.tools.dash.business.jenkins.job;

import de.axelspringer.ideas.tools.dash.business.check.CheckResult;
import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.business.jenkins.JenkinsClient;
import de.axelspringer.ideas.tools.dash.business.jenkins.JenkinsServerConfiguration;
import de.axelspringer.ideas.tools.dash.business.jenkins.joblist.JenkinsJobNameMapper;
import de.axelspringer.ideas.tools.dash.business.jenkins.domain.JenkinsJobInfo;
import de.axelspringer.ideas.tools.dash.presentation.State;
import org.apache.http.auth.AuthenticationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JenkinsJobCheckExecutorTest {

    @Mock
    private JenkinsClient jenkinsClient;

    @InjectMocks
    private JenkinsJobCheckExecutor jenkinsJobCheckExecutor;

    @Before
    public void initMocks() throws Exception {
        when(jenkinsClient.queryApi(anyString(), any(JenkinsServerConfiguration.class), eq(JenkinsJobInfo.class))).thenReturn(jenkinsJobInfo());
    }

    @Test
    public void testGetShortNameWithoutJobNameMapper() {

        JenkinsJobCheck jenkinsJobCheck = jenkinsCheck("my-name-is-kept-untouched", null);

        final String shortName = jenkinsJobCheckExecutor.shortName(jenkinsJobCheck);

        assertEquals("my-name-is-kept-untouched", shortName);
    }

    @Test
    public void testGetShortNameWithJobNameMapper() {

        JenkinsJobCheck jenkinsJobCheck = jenkinsCheck("My-Name-Is-Lower-Cased", (check) -> check.getName().toLowerCase());

        final String shortName = jenkinsJobCheckExecutor.shortName(jenkinsJobCheck);

        assertEquals("my-name-is-lower-cased", shortName);
    }

    @Test
    public void testBuildWithoutLastBuildResultResultsInGreenState() throws IOException, AuthenticationException {

        final List<CheckResult> checkResults = jenkinsJobCheckExecutor.executeCheck(jenkinsCheck());

        assertEquals(1, checkResults.size());
        final CheckResult checkResult = checkResults.get(0);
        assertEquals(State.GREEN, checkResult.getState());
    }

    private JenkinsJobInfo jenkinsJobInfo() {
        JenkinsJobInfo jenkinsJobInfo = new JenkinsJobInfo();
        Whitebox.setInternalState(jenkinsJobInfo, "buildable", true);
        return jenkinsJobInfo;
    }

    private JenkinsJobCheck jenkinsCheck() {
        return jenkinsCheck("", null);
    }

    private JenkinsJobCheck jenkinsCheck(String name, JenkinsJobNameMapper jenkinsJobNameMapper) {
        return new JenkinsJobCheck(name, "", "", "", mock(Group.class), mock(List.class), jenkinsJobNameMapper, "");
    }
}