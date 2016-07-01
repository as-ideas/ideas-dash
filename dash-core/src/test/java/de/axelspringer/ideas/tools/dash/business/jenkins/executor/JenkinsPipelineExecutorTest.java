package de.axelspringer.ideas.tools.dash.business.jenkins.executor;

import de.axelspringer.ideas.tools.dash.business.check.CheckResult;
import de.axelspringer.ideas.tools.dash.business.jenkins.JenkinsCheck;
import de.axelspringer.ideas.tools.dash.business.jenkins.JenkinsClient;
import de.axelspringer.ideas.tools.dash.business.jenkins.JenkinsServerConfiguration;
import de.axelspringer.ideas.tools.dash.business.jenkins.domain.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JenkinsPipelineExecutorTest {

    private final static String LAST_BUILD_URL = "/foo/lastbuild";

    private final static String LAST_SUCCESSFUL_BUILD_URL = "/foo/lastsuccessfulbuild";

    @Mock
    private JenkinsClient client;

    @InjectMocks
    private JenkinsPipelineExecutor executor;

    private final Build lastSuccessfulBuild = new Build(LAST_SUCCESSFUL_BUILD_URL);

    private final Build lastBuild = new Build(LAST_BUILD_URL);

    @Test
    public void testExecuteCheckWithValidPipelineDefinitionInLastSuccessfulExecution() throws Exception {

        initMockForUrl(LAST_BUILD_URL, buildInfo("stage one", "stage two"));
        initMockForUrl(LAST_SUCCESSFUL_BUILD_URL, buildInfo("stage one", "stage two", "stage three"));

        final List<CheckResult> checkResults = executor.executeCheck(new JenkinsJobInfo(lastSuccessfulBuild, lastBuild), mock(JenkinsCheck.class));
        assertEquals(3, checkResults.size());
        assertEquals("NOT EXECUTED", checkResults.get(2).getInfo());
    }

    @Test
    public void testExecuteCheckWithOutdatedPipelineDefinitionInLastSuccessfulExecution() throws Exception {

        initMockForUrl(LAST_BUILD_URL, buildInfo("stage one", "stage newTwo"));
        initMockForUrl(LAST_SUCCESSFUL_BUILD_URL, buildInfo("stage one", "stage two", "stage three"));

        final List<CheckResult> checkResults = executor.executeCheck(new JenkinsJobInfo(lastSuccessfulBuild, lastBuild), mock(JenkinsCheck.class));
        assertEquals(2, checkResults.size());
    }

    @Test
    public void testExecuteCheckWithNoLastSuccessfulExecution() throws Exception {

        initMockForUrl(LAST_BUILD_URL, buildInfo("stage one", "stage two"));

        final List<CheckResult> checkResults = executor.executeCheck(new JenkinsJobInfo(null, lastBuild), mock(JenkinsCheck.class));
        assertEquals(2, checkResults.size());
    }

    private void initMockForUrl(String url, JenkinsPipelineBuildInfo buildInfo) {
        when(client.queryWorkflowApi(eq(url), any(JenkinsServerConfiguration.class), eq(JenkinsPipelineBuildInfo.class)))
                .thenReturn(buildInfo);
    }

    private JenkinsPipelineBuildInfo buildInfo(String... stages) {

        final List<PipelineStage> pipelineStages = Stream.of(stages)
                .map(PipelineStage::new)
                .collect(Collectors.toList());
        return new JenkinsPipelineBuildInfo(JenkinsResult.SUCCESS, pipelineStages);
    }
}