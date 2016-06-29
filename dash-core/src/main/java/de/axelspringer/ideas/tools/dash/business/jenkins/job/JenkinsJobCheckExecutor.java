package de.axelspringer.ideas.tools.dash.business.jenkins.job;

import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.check.CheckExecutor;
import de.axelspringer.ideas.tools.dash.business.check.CheckResult;
import de.axelspringer.ideas.tools.dash.business.jenkins.JenkinsClient;
import de.axelspringer.ideas.tools.dash.business.jenkins.JenkinsServerConfiguration;
import de.axelspringer.ideas.tools.dash.business.jenkins.domain.JenkinsBuildInfo;
import de.axelspringer.ideas.tools.dash.business.jenkins.domain.JenkinsJobInfo;
import de.axelspringer.ideas.tools.dash.presentation.State;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@Service
public class JenkinsJobCheckExecutor implements CheckExecutor<JenkinsJobCheck> {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(JenkinsJobCheckExecutor.class);

    @Autowired
    private JenkinsClient jenkinsClient;

    @Override
    public List<CheckResult> executeCheck(JenkinsJobCheck jenkinsJobCheck) {

        final String jobName = jenkinsJobCheck.getName();

        // load results from jenkins
        final JenkinsBuildInfo lastCompletedBuildInfo;
        final JenkinsBuildInfo lastBuildInfo;
        final JenkinsServerConfiguration serverConfig = jenkinsJobCheck.getServerConfiguration();

        try {
            log.debug("Retrieving job result from jenkins url {}", jenkinsJobCheck.getJobUrl());

            final JenkinsJobInfo jobInfo = jenkinsClient.queryApi(jenkinsJobCheck.getJobUrl(), serverConfig, JenkinsJobInfo.class);

            final JenkinsJobInfo.Build lastCompletedBuild = jobInfo.getLastCompletedBuild();
            lastCompletedBuildInfo = lastCompletedBuild != null ? jenkinsClient.queryApi(lastCompletedBuild.getUrl(), serverConfig, JenkinsBuildInfo.class) : null;

            final JenkinsJobInfo.Build lastBuild = jobInfo.getLastBuild();
            lastBuildInfo = lastBuild != null ? jenkinsClient.queryApi(lastBuild.getUrl(), serverConfig, JenkinsBuildInfo.class) : null;

            if (!jobInfo.isBuildable()) {
                return Collections.emptyList();
            }

        } catch (Exception e) {
            log.error("error fetching jenkins result: {}", jobName, e);
            return Collections.singletonList(new CheckResult(State.RED, shortName(jenkinsJobCheck), "N/A", 0, 0, jenkinsJobCheck.getGroup()).withLink(jenkinsJobCheck.getJobUrl()).withTeams(jenkinsJobCheck.getTeams()));
        }

        int failedTestCount = 0;
        int totalTestCount = 0;

        if (lastCompletedBuildInfo != null) {
            for (JenkinsBuildInfo.Action action : lastCompletedBuildInfo.getActions()) {
                if (action.getFailCount() != null && action.getTotalCount() != null) {
                    failedTestCount += action.getFailCount();
                    totalTestCount += action.getTotalCount();
                }
            }
        }

        final String checkInfo = failedTestCount > 0 ? failedTestCount + "/" + totalTestCount : "" + totalTestCount;

        State state = identifyStatus(lastCompletedBuildInfo, failedTestCount);
        CheckResult checkResult = new CheckResult(state, shortName(jenkinsJobCheck), checkInfo, totalTestCount, failedTestCount, jenkinsJobCheck.getGroup()).withLink(jenkinsJobCheck.getJobUrl()).withTeams(jenkinsJobCheck.getTeams());
        if (lastBuildInfo != null && lastBuildInfo.isBuilding()) {
            checkResult = checkResult.markRunning();
        }
        return Collections.singletonList(checkResult);
    }

    @Override
    public boolean isApplicable(Check check) {
        return check.getClass() == JenkinsJobCheck.class;
    }

    private State identifyStatus(JenkinsBuildInfo jenkinsBuildInfo, int failedTestCount) {

        if (failedTestCount > 0) {
            return State.YELLOW;
        }

        if (jenkinsBuildInfo == null) {
            // never executed = fine
            return State.GREEN;
        }

        if (jenkinsBuildInfo.getResult() == null) {
            return State.RED;
        }

        switch (jenkinsBuildInfo.getResult()) {
            case ABORTED:
                return State.GREY;
            case UNSTABLE:
                // if there were only test failures, we never get here. therefore treat unstable as failed
            case FAILURE:
                return State.YELLOW;
            case SUCCESS:
                return State.GREEN;
            default:
                return State.GREY;
        }
    }

    String shortName(JenkinsJobCheck check) {
        if (check.getJenkinsJobNameMapper() != null) {
            return check.getJenkinsJobNameMapper().map(check);
        }
        return check.getName();
    }
}
