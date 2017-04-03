package de.axelspringer.ideas.tools.dash.business.jenkins.executor;

import de.axelspringer.ideas.tools.dash.business.jenkins.domain.JenkinsJobInfo;
import de.axelspringer.ideas.tools.dash.business.jenkins.domain.Property;
import de.axelspringer.ideas.tools.dash.presentation.State;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.*;

public class JenkinsJobToStateMapperTest {

    private JenkinsJobToStateMapper mapper = new JenkinsJobToStateMapper();

    @Test
    public void decreaseSeverityMultiBranchMaster() throws Exception {

        assertFalse(mapper.decreaseSeverity(jobInfo(true, true)));
    }

    @Test
    public void decreaseSeverityMultiBranchFeatureBranch() throws Exception {

        assertTrue(mapper.decreaseSeverity(jobInfo(true, false)));
    }

    @Test
    public void decreaseSeverityNonMultiBranchFeatureBranch() throws Exception {

        assertFalse(mapper.decreaseSeverity(jobInfo(false, false)));
    }

    @Test
    public void decreaseSeverityNonMultiBranchMaster() throws Exception {

        assertFalse(mapper.decreaseSeverity(jobInfo(false, true)));
    }

    @Test
    public void identifyStatusFailedTestCountRegularSeverity() throws Exception {

        assertEquals(State.YELLOW, mapper.identifyStatus(null, 1, jobInfo(false, true)));
    }

    @Test
    public void identifyStatusFailedTestCountDecreasedSeverity() throws Exception {

        assertEquals(State.GREY, mapper.identifyStatus(null, 1, jobInfo(true, false)));
    }

    private JenkinsJobInfo jobInfo(boolean multibranch, boolean master) {

        final JenkinsJobInfo jobInfo = new JenkinsJobInfo();
        jobInfo.setName(master ? "master" : "featureABC");
        jobInfo.setProperties(Collections.singletonList(new Property(multibranch ? Property.MULTIBRANCH_CLASS : "SomeOtherClass")));
        return jobInfo;
    }
}