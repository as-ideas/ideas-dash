package de.axelspringer.ideas.tools.dash.business.jenkins;

import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.check.CheckExecutor;
import de.axelspringer.ideas.tools.dash.business.check.CheckResult;
import de.axelspringer.ideas.tools.dash.presentation.State;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.auth.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


@Service
@Slf4j
public class JenkinsCheckExecutor implements CheckExecutor<JenkinsCheck> {

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

            lastCompletedBuildInfo = queryJenkins(jenkinsCheck, jobInfo.getLastCompletedBuild().getUrl(), JenkinsBuildInfo.class);
            lastBuildInfo = queryJenkins(jenkinsCheck, jobInfo.getLastBuild().getUrl(), JenkinsBuildInfo.class);

        } catch (Exception e) {
            log.error("error fetching jenkins result: {}", jobName, e);
            return Arrays.asList(new CheckResult(State.RED, shortName(jobName), "N/A", 0, 0, jenkinsCheck.getGroup()).withLink(url).withTeam(jenkinsCheck.getTeam()));
        }

        int failedTestCount = 0;
        int totalTestCount = 0;

        for (JenkinsBuildInfo.Action action : lastCompletedBuildInfo.getActions()) {
            if (action.getFailCount() != null && action.getTotalCount() != null) {
                failedTestCount += action.getFailCount();
                totalTestCount += action.getTotalCount();
            }
        }

        final String checkInfo = failedTestCount > 0 ? failedTestCount + "/" + totalTestCount : "" + totalTestCount;

        State state = identifyStatus(lastCompletedBuildInfo, failedTestCount);
        CheckResult checkResult = new CheckResult(state, shortName(jobName), checkInfo, totalTestCount, failedTestCount, jenkinsCheck.getGroup()).withLink(url).withTeam(jenkinsCheck.getTeam());
        if (lastBuildInfo.isBuilding()) {
            checkResult = checkResult.markRunning();
        }
        return Arrays.asList(checkResult);
    }

    @Override
    public boolean isApplicable(Check check) {
        return check instanceof JenkinsCheck;
    }

    private State identifyStatus(JenkinsBuildInfo lastSuccessfullBuildInfo, int failedTestCount) {

        if (failedTestCount > 0) {
            return State.YELLOW;
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
