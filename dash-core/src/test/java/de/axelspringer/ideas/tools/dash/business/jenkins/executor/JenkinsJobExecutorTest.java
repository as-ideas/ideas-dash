package de.axelspringer.ideas.tools.dash.business.jenkins.executor;

import de.axelspringer.ideas.tools.dash.business.check.checkresult.CheckResult;
import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.business.jenkins.JenkinsCheck;
import de.axelspringer.ideas.tools.dash.business.jenkins.JenkinsClient;
import de.axelspringer.ideas.tools.dash.business.jenkins.JenkinsServerConfiguration;
import de.axelspringer.ideas.tools.dash.business.jenkins.domain.BuildInfo;
import de.axelspringer.ideas.tools.dash.business.jenkins.domain.JenkinsJobInfo;
import de.axelspringer.ideas.tools.dash.business.jenkins.joblist.JenkinsJobNameMapper;
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
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JenkinsJobExecutorTest {

    @Mock
    private JenkinsClient jenkinsClient;

    @InjectMocks
    private JenkinsJobExecutor jenkinsJobExecutor;

    @Before
    public void initMocks() throws Exception {
        when(jenkinsClient.queryApi(anyString(), any(JenkinsServerConfiguration.class), eq(JenkinsJobInfo.class))).thenReturn(jenkinsJobInfo());
    }

    @Test
    public void testGetShortNameWithoutJobNameMapper() {

        JenkinsCheck jenkinsCheck = jenkinsCheck("my-name-is-kept-untouched", null);

        final String shortName = jenkinsJobExecutor.shortName(jenkinsCheck);

        assertEquals("my-name-is-kept-untouched", shortName);
    }

    @Test
    public void testGetShortNameWithJobNameMapper() {

        JenkinsCheck jenkinsCheck = jenkinsCheck("My-Name-Is-Lower-Cased", (check) -> check.getName().toLowerCase());

        final String shortName = jenkinsJobExecutor.shortName(jenkinsCheck);

        assertEquals("my-name-is-lower-cased", shortName);
    }

    @Test
    public void testBuildWithoutLastBuildResultResultsInGreenState() throws IOException, AuthenticationException {

        final List<CheckResult> checkResults = jenkinsJobExecutor.executeCheck(jenkinsJobInfo(), jenkinsCheck(),mock(BuildInfo.class));

        assertEquals(1, checkResults.size());
        final CheckResult checkResult = checkResults.get(0);
        assertEquals(State.GREEN, checkResult.getState());
    }

    private JenkinsJobInfo jenkinsJobInfo() {
        JenkinsJobInfo jenkinsJobInfo = new JenkinsJobInfo();
        Whitebox.setInternalState(jenkinsJobInfo, "buildable", true);
        return jenkinsJobInfo;
    }

    private JenkinsCheck jenkinsCheck() {
        return jenkinsCheck("", null);
    }

    private JenkinsCheck jenkinsCheck(String name, JenkinsJobNameMapper jenkinsJobNameMapper) {
        return new JenkinsCheck(name, "", "", "", mock(Group.class), mock(List.class), jenkinsJobNameMapper, "");
    }
}