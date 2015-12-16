package de.axelspringer.ideas.tools.dash.business.jenkins;

import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.check.CheckExecutor;
import de.axelspringer.ideas.tools.dash.business.check.CheckResult;
import de.axelspringer.ideas.tools.dash.presentation.State;
import org.apache.http.auth.AuthenticationException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;


@Service
public class JenkinsCheckExecutor implements CheckExecutor<JenkinsCheck> {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(JenkinsCheckExecutor.class);

    @Autowired
    private JenkinsClient jenkinsClient;

    @Override
    public List<CheckResult> executeCheck(JenkinsCheck jenkinsCheck) {

        final String jobName = jenkinsCheck.getName();

        // load results from jenkins
        final JenkinsBuildInfo lastCompletedBuildInfo;
        final JenkinsBuildInfo lastBuildInfo;
        final String url = jenkinsCheck.getUrl();

        try {
            log.debug("Retrieving job result from jenkins url {}", url);

            final JenkinsJobInfo jobInfo = queryJenkins(jenkinsCheck, url, JenkinsJobInfo.class);

            final JenkinsJobInfo.LastBuild lastCompletedBuild = jobInfo.getLastCompletedBuild();
            lastCompletedBuildInfo = lastCompletedBuild != null ? queryJenkins(jenkinsCheck, lastCompletedBuild.getUrl(), JenkinsBuildInfo.class) : null;

            final JenkinsJobInfo.LastBuild lastBuild = jobInfo.getLastBuild();
            lastBuildInfo = lastBuild != null ? queryJenkins(jenkinsCheck, lastBuild.getUrl(), JenkinsBuildInfo.class) : null;

            if (!jobInfo.isBuildable()) {
                return Collections.emptyList();
            }

        } catch (Exception e) {
            log.error("error fetching jenkins result: {}", jobName, e);
            return Collections.singletonList(new CheckResult(State.RED, shortName(jobName), "N/A", 0, 0, jenkinsCheck.getGroup()).withLink(url).withTeam(jenkinsCheck.getTeam()));
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
        CheckResult checkResult = new CheckResult(state, shortName(jobName), checkInfo, totalTestCount, failedTestCount, jenkinsCheck.getGroup()).withLink(url).withTeam(jenkinsCheck.getTeam());
        if (lastBuildInfo != null && lastBuildInfo.isBuilding()) {
            checkResult = checkResult.markRunning();
        }
        return Collections.singletonList(checkResult);
    }

    @Override
    public boolean isApplicable(Check check) {
        return check instanceof JenkinsCheck;
    }

    private State identifyStatus(JenkinsBuildInfo lastSuccessfullBuildInfo, int failedTestCount) {

        if (failedTestCount > 0) {
            return State.YELLOW;
        }

        if (lastSuccessfullBuildInfo == null) {
            return State.GREY;
        }

        if (lastSuccessfullBuildInfo.getResult() == null) {
            return State.RED;
        }

        switch (lastSuccessfullBuildInfo.getResult()) {
            case ABORTED:
                return State.GREY;
            case UNSTABLE:
                // if there were only test failures, we never get here. therefore treat unstable as failed
            case FAILURE:
                return State.RED;
            case SUCCESS:
                return State.GREEN;
            default:
                return State.GREY;
        }
    }

    private <T> T queryJenkins(JenkinsCheck jenkinsCheck, String url, Class<T> responseType) throws IOException, AuthenticationException {
        try {
            return jenkinsClient.query(url, jenkinsCheck.getUserName(), jenkinsCheck.getApiToken(), responseType);
        } catch (Exception e) {
            log.error("Could not load job from jenkins. URL=" + url);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    String shortName(String jobName) {

        String[] splitJobName = jobName.split("_");
        if (splitJobName.length < 2) {
            return jobName;
        }
        return splitJobName[1];
    }
}
