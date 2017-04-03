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

import static de.axelspringer.ideas.tools.dash.presentation.State.GREEN;
import static de.axelspringer.ideas.tools.dash.presentation.State.RED;


@Service
public class JenkinsJobExecutor {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(JenkinsJobExecutor.class);

    @Autowired
    private JenkinsClient jenkinsClient;

    @Autowired
    private JenkinsJobToStateMapper stateMapper;

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
            return Collections.singletonList(new CheckResult(RED, shortName(jenkinsCheck), "N/A", 0, 0, jenkinsCheck.getGroup()).withLink(jenkinsCheck.getJobUrl()).withTeams(jenkinsCheck.getTeams()));
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

        final State state =
                // never executed -> no problems :D
                lastCompletedBuildInfo == null ? GREEN :
                        // let the statemapper do its magic
                        stateMapper.identifyStatus(lastCompletedBuildInfo.getResult(), failedTestCount, jobInfo);

        CheckResult checkResult = new CheckResult(state, shortName(jenkinsCheck), checkInfo, totalTestCount, failedTestCount, jenkinsCheck.getGroup()).withLink(jenkinsCheck.getJobUrl()).withTeams(jenkinsCheck.getTeams());
        if (lastBuildInfo != null && lastBuildInfo.isBuilding()) {
            checkResult = checkResult.markRunning();
        }
        return Collections.singletonList(checkResult);
    }


    String shortName(JenkinsCheck check) {
        if (check.getJenkinsJobNameMapper() != null) {
            return check.getJenkinsJobNameMapper().map(check);
        }
        return check.getName();
    }
}
