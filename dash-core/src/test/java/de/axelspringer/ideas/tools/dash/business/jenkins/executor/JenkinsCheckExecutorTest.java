package de.axelspringer.ideas.tools.dash.business.jenkins.executor;

import de.axelspringer.ideas.tools.dash.business.jenkins.JenkinsCheck;
import de.axelspringer.ideas.tools.dash.business.jenkins.JenkinsClient;
import de.axelspringer.ideas.tools.dash.business.jenkins.JenkinsServerConfiguration;
import de.axelspringer.ideas.tools.dash.business.jenkins.domain.JenkinsJobInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class JenkinsCheckExecutorTest {

    @Mock
    private JenkinsJobExecutor jobExecutor;

    @Mock
    private JenkinsPipelineExecutor pipelineExecutor;

    @Mock
    private JenkinsClient jenkinsClient;

    @InjectMocks
    private JenkinsCheckExecutor jenkinsCheckExecutor;

    @Test
    public void testExecuteCheckAgainstPipelineJobExecutesPipelineExecutor() throws Exception {

        executeWithClass(JenkinsJobInfo.PIPELINE_CLASS);

        verifyZeroInteractions(jobExecutor);
        verify(pipelineExecutor, times(1)).executeCheck(any(JenkinsJobInfo.class), any(JenkinsCheck.class));
    }

    @Test
    public void testExecuteCheckAgainstRegularJobExecutesJobExecutor() throws Exception {

        executeWithClass("some-other-class");

        verifyZeroInteractions(pipelineExecutor);
        verify(jobExecutor, times(1)).executeCheck(any(JenkinsJobInfo.class), any(JenkinsCheck.class));
    }

    private void executeWithClass(String buildClass) {
        final JenkinsJobInfo jobInfo = new JenkinsJobInfo();
        jobInfo.setBuildClass(buildClass);
        when(jenkinsClient.queryApi(anyString(), any(JenkinsServerConfiguration.class), eq(JenkinsJobInfo.class)))
                .thenReturn(jobInfo);
        jenkinsCheckExecutor.executeCheck(mock(JenkinsCheck.class));
    }
}