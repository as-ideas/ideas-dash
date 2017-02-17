package de.axelspringer.ideas.tools.dash.business.jenkins.executor;

import de.axelspringer.ideas.tools.dash.business.jenkins.JenkinsCheck;
import de.axelspringer.ideas.tools.dash.business.jenkins.JenkinsClient;
import de.axelspringer.ideas.tools.dash.business.jenkins.JenkinsServerConfiguration;
import de.axelspringer.ideas.tools.dash.business.jenkins.domain.BuildInfo;
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

        execute(JenkinsJobInfo.PIPELINE_CLASS, true);

        verifyZeroInteractions(jobExecutor);
        verify(pipelineExecutor, times(1)).executeCheck(any(JenkinsJobInfo.class), any(JenkinsCheck.class), any(BuildInfo.class));
    }

    @Test
    public void testExecuteCheckAgainstPipelineJobButNonExplodedPipelinesExecutesJobExecutor() throws Exception {

        execute(JenkinsJobInfo.PIPELINE_CLASS, false);

        verifyZeroInteractions(pipelineExecutor);
        verify(jobExecutor, times(1)).executeCheck(any(JenkinsJobInfo.class), any(JenkinsCheck.class), any(BuildInfo.class));
    }

    @Test
    public void testExecuteCheckAgainstRegularJobExecutesJobExecutor() throws Exception {

        execute("some-other-class", true);

        verifyZeroInteractions(pipelineExecutor);
        verify(jobExecutor, times(1)).executeCheck(any(JenkinsJobInfo.class), any(JenkinsCheck.class), any(BuildInfo.class));
    }

    private void execute(String withBuildClass, boolean withExplodePipeline) {
        final JenkinsJobInfo jobInfo = new JenkinsJobInfo();
        jobInfo.setBuildClass(withBuildClass);
        when(jenkinsClient.queryApi(anyString(), any(JenkinsServerConfiguration.class), eq(JenkinsJobInfo.class)))
                .thenReturn(jobInfo);
        final JenkinsCheck check = mock(JenkinsCheck.class);
        when(check.isExplodePipelines()).thenReturn(withExplodePipeline);
        jenkinsCheckExecutor.executeCheck(check);
    }
}