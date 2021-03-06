package de.axelspringer.ideas.tools.dash.business.jenkins.executor;

import de.axelspringer.ideas.tools.dash.business.jenkins.domain.JenkinsBuildInfo;
import de.axelspringer.ideas.tools.dash.business.jenkins.domain.JenkinsJobInfo;
import de.axelspringer.ideas.tools.dash.business.jenkins.domain.JenkinsResult;
import de.axelspringer.ideas.tools.dash.business.jenkins.domain.Property;
import de.axelspringer.ideas.tools.dash.presentation.State;
import org.springframework.stereotype.Service;

import static de.axelspringer.ideas.tools.dash.presentation.State.*;

@Service
public class JenkinsJobToStateMapper {

    /**
     * Helps mapping jenkins job information to a {@link State}
     */
    public State identifyStatus(JenkinsBuildInfo buildInfo, int failedTestCount, JenkinsJobInfo jobInfo) {

        if (buildInfo == null) {
            return GREEN;
        }

        if (failedTestCount > 0) {
            return isMaster(jobInfo) ? YELLOW : isActive(buildInfo) ? GREEN : GREY;
        }

        JenkinsResult result = buildInfo.getResult();
        if (result == null) {
            return isMaster(jobInfo) ? RED : isActive(buildInfo) ? GREY : YELLOW;
        }

        switch (result) {
            case ABORTED:
                return isMaster(jobInfo) ? GREY : isActive(buildInfo) ? GREEN : GREY;
            case UNSTABLE:
                // if there were only test failures, we never get here. therefore treat unstable as failed
            case FAILURE:
                return isMaster(jobInfo) ? YELLOW : isActive(buildInfo) ? GREEN : GREY;
            case SUCCESS:
                return State.GREEN;
            default:
                return GREY;
        }
    }

    private boolean isActive(JenkinsBuildInfo buildInfo) {
        long oneWeekAgo = System.currentTimeMillis() - (7 * 24 * 3600 * 1000);
        return buildInfo.getTimestamp() > oneWeekAgo;
    }

    private boolean isMaster(JenkinsJobInfo jobInfo) {
        boolean isMultiBranch = jobInfo.getProperties()
                .stream()
                .anyMatch(property -> Property.MULTIBRANCH_CLASS.equals(property.getPropertyClass()));
        if (!isMultiBranch) {
            // non multi-branch jobs always are the 'master'
            return true;
        }
        return "master".equalsIgnoreCase(jobInfo.getName());
    }
}
