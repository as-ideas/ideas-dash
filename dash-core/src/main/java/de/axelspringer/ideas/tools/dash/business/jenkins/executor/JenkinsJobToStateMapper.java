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

        final boolean decreaseSeverity = decreaseSeverity(jobInfo);

        if (failedTestCount > 0) {
            return decreaseSeverity ? GREY : YELLOW;
        }

        JenkinsResult result = buildInfo.getResult();
        if (result == null) {
            return decreaseSeverity ? YELLOW : RED;
        }

        switch (result) {
            case ABORTED:
                return GREY;
            case UNSTABLE:
                // if there were only test failures, we never get here. therefore treat unstable as failed
            case FAILURE:
                return decreaseSeverity ? GREY : YELLOW;
            case SUCCESS:
                return State.GREEN;
            default:
                return GREY;
        }
    }

    /**
     * On Multibranch-Projects we need to treat non-master-branches differently (less strict). This method tries to solve this.
     */
    boolean decreaseSeverity(JenkinsJobInfo jobInfo) {

        final boolean isMultiBranch =
                jobInfo.getProperties()
                        .stream()
                        .anyMatch(property -> Property.MULTIBRANCH_CLASS.equals(property.getPropertyClass()));

        final boolean isMaster = "master".equalsIgnoreCase(jobInfo.getName());

        return isMultiBranch && !isMaster;
    }
}
