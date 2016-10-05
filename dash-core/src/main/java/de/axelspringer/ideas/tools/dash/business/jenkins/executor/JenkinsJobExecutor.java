package de.axelspringer.ideas.tools.dash.business.jenkins.executor;

import de.axelspringer.ideas.tools.dash.business.check.checkresult.CheckResult;
import de.axelspringer.ideas.tools.dash.business.jenkins.JenkinsCheck;
import de.axelspringer.ideas.tools.dash.business.jenkins.JenkinsClient;
import de.axelspringer.ideas.tools.dash.business.jenkins.JenkinsServerConfiguration;
import de.axelspringer.ideas.tools.dash.business.jenkins.domain.*;
import de.axelspringer.ideas.tools.dash.presentation.State;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@Service
public class JenkinsJobExecutor {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(JenkinsJobExecutor.class);

    @Autowired
    private JenkinsClient jenkinsClient;

    public List<CheckResult> executeCheck(JenkinsJobInfo jobInfo, JenkinsCheck jenkinsCheck, BuildInfo buildInfo) {

        final String jobName = jenkinsCheck.getName();

        // load results from jenkins
        final JenkinsBuildInfo lastCompletedBuildInfo;
        final JenkinsBuildInfo lastBuildInfo;
        final JenkinsServerConfiguration serverConfig = jenkinsCheck.getServerConfiguration();

        try {
            log.debug("Retrieving job result from jenkins url {}", jenkinsCheck.getJobUrl());

            final Build lastCompletedBuild = jobInfo.getLastCompletedBuild();
            lastCompletedBuildInfo = lastCompletedBuild != null ? jenkinsClient.queryApi(lastCompletedBuild.getUrl(), serverConfig, JenkinsBuildInfo.class) : null;

            final Build lastBuild = jobInfo.getLastBuild();
            lastBuildInfo = lastBuild != null ? jenkinsClient.queryApi(lastBuild.getUrl(), serverConfig, JenkinsBuildInfo.class) : null;

            if (!jobInfo.isBuildable()) {
                return Collections.emptyList();
            }

        } catch (Exception e) {
            log.error("error fetching jenkins result: {}", jobName, e);
            return Collections.singletonList(new CheckResult(State.RED, shortName(jenkinsCheck), "N/A", 0, 0, jenkinsCheck.getGroup()).withLink(jenkinsCheck.getJobUrl()).withTeams(jenkinsCheck.getTeams()));
        }

        int failedTestCount = 0;
        int totalTestCount = 0;

        if (lastCompletedBuildInfo != null) {
            for (JenkinsBuildAction action : lastCompletedBuildInfo.getActions()) {
                if (action.getFailCount() != null && action.getTotalCount() != null) {
                    failedTestCount += action.getFailCount();
                    totalTestCount += action.getTotalCount();
                }
            }
        }

        final String checkInfo = failedTestCount > 0 ? failedTestCount + "/" + totalTestCount : "" + totalTestCount;

        State state = identifyStatus(lastCompletedBuildInfo, failedTestCount);
        CheckResult checkResult = new CheckResult(state, shortName(jenkinsCheck), checkInfo, totalTestCount, failedTestCount, jenkinsCheck.getGroup()).withLink(jenkinsCheck.getJobUrl()).withTeams(jenkinsCheck.getTeams());
        if (lastBuildInfo != null && lastBuildInfo.isBuilding()) {
            checkResult = checkResult.markRunning();
        }
        return Collections.singletonList(checkResult);
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

    String shortName(JenkinsCheck check) {
        if (check.getJenkinsJobNameMapper() != null) {
            return check.getJenkinsJobNameMapper().map(check);
        }
        return check.getName();
    }
}
