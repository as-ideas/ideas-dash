package de.axelspringer.ideas.tools.dash.business.jenkins;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;
import org.apache.http.auth.AuthenticationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;
import de.axelspringer.ideas.tools.dash.business.check.CheckResult;
import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.presentation.State;

@RunWith(MockitoJUnitRunner.class)
public class JenkinsCheckExecutorTest {

    @Mock
    private JenkinsClient jenkinsClient;

    @InjectMocks
    private JenkinsCheckExecutor jenkinsCheckExecutor;

    @Before
    public void initMocks() throws Exception {
        when(jenkinsClient.query(anyString(), anyString(), anyString(), eq(JenkinsJobInfo.class))).thenReturn(jenkinsJobInfo());
    }

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

    @Test
    public void testBuildWithoutLastBuildResultResultsInGreyState() throws IOException, AuthenticationException {

        final List<CheckResult> checkResults = jenkinsCheckExecutor.executeCheck(jenkinsCheck());

        assertEquals(1, checkResults.size());
        final CheckResult checkResult = checkResults.get(0);
        assertEquals(State.GREY, checkResult.getState());
    }

    private JenkinsJobInfo jenkinsJobInfo() {
        JenkinsJobInfo jenkinsJobInfo = new JenkinsJobInfo();
        Whitebox.setInternalState(jenkinsJobInfo, "buildable", true);
        return jenkinsJobInfo;
    }

    private JenkinsCheck jenkinsCheck() {
        return new JenkinsCheck("", "", "", "", mock(Group.class), mock(List.class));
    }
}