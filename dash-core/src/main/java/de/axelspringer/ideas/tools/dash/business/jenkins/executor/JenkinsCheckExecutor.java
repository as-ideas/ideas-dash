package de.axelspringer.ideas.tools.dash.business.jenkins.executor;

import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.check.CheckExecutor;
import de.axelspringer.ideas.tools.dash.business.check.checkresult.CheckResult;
import de.axelspringer.ideas.tools.dash.business.jenkins.JenkinsCheck;
import de.axelspringer.ideas.tools.dash.business.jenkins.JenkinsClient;
import de.axelspringer.ideas.tools.dash.business.jenkins.JenkinsServerConfiguration;
import de.axelspringer.ideas.tools.dash.business.jenkins.domain.BuildInfo;
import de.axelspringer.ideas.tools.dash.business.jenkins.domain.JenkinsJobInfo;
import de.axelspringer.ideas.tools.dash.presentation.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class JenkinsCheckExecutor implements CheckExecutor<JenkinsCheck> {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final JenkinsJobExecutor jobExecutor;

    private final JenkinsPipelineExecutor pipelineExecutor;

    private final JenkinsClient jenkinsClient;

    @Autowired
    public JenkinsCheckExecutor(JenkinsJobExecutor jobExecutor, JenkinsPipelineExecutor pipelineExecutor, JenkinsClient jenkinsClient) {

        this.jobExecutor = jobExecutor;
        this.pipelineExecutor = pipelineExecutor;
        this.jenkinsClient = jenkinsClient;
    }

    @Override
    public List<CheckResult> executeCheck(JenkinsCheck check) {

        final JenkinsServerConfiguration serverConfiguration = check.getServerConfiguration();
        final JenkinsJobInfo jobInfo = jenkinsClient.queryApi(check.getJobUrl(), serverConfiguration, JenkinsJobInfo.class);

        if (jobInfo == null) {
            log.error("error fetching jenkins result. Job query returned null: {}", check.getName());
            return Collections.singletonList(
                    new CheckResult(State.RED, check.getName(), "N/A", 0, 0, check.getGroup())
                            .withLink(check.getJobUrl())
                            .withTeams(check.getTeams()));
        }

        final BuildInfo buildInfo = buildInfo(check.getJobUrl(), serverConfiguration);

        if (jobInfo.isPipeline()) {
            return pipelineExecutor.executeCheck(jobInfo, check, buildInfo);
        }
        return jobExecutor.executeCheck(jobInfo, check, buildInfo);
    }

    private BuildInfo buildInfo(String jobUrl, JenkinsServerConfiguration serverConfiguration) {
        final BuildInfo buildInfo = jenkinsClient.query(
                jobUrl + "/lastSuccessfulBuild/artifact/" + BuildInfo.JENKINS_BUILD_INFO_FILE_NAME, serverConfiguration,
                BuildInfo.class);
        return buildInfo != null ? buildInfo : new BuildInfo();
    }

    @Override
    public boolean isApplicable(Check check) {
        return check instanceof JenkinsCheck;
    }
}
