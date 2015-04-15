package de.axelspringer.ideas.tools.dash.business.jenkins;

import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.business.customization.Team;
import org.apache.http.auth.AuthenticationException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JenkinsJobListCheckProviderTest {

    private JenkinsJobListCheckProvider jenkinsJobListCheckProvider;

    @Before
    public void initMocks() throws IOException, AuthenticationException {

        jenkinsJobListCheckProvider = new JenkinsJobListCheckProvider("fooHost", "fooUser", "fooApiToken", mock(Group.class));

        final List<JenkinsJob> jobs = new ArrayList<>();
        jobs.add(jenkinsJob("docker-compose"));
        jobs.add(jenkinsJob("yana-contentmachine-build"));
        jobs.add(jenkinsJob("reco-svc-recommendations-build"));
        final JenkinsJobListWrapper jenkinsJobListWrapper = new JenkinsJobListWrapper();
        jenkinsJobListWrapper.setJobs(jobs);

        final JenkinsClient jenkinsClient = mock(JenkinsClient.class);
        when(jenkinsClient.query(anyString(), anyString(), anyString(), eq(JenkinsJobListWrapper.class))).thenReturn(jenkinsJobListWrapper);

        ReflectionTestUtils.setField(jenkinsJobListCheckProvider, "jenkinsClient", jenkinsClient);
    }

    private JenkinsJob jenkinsJob(String name) {
        final JenkinsJob jenkinsJob = new JenkinsJob();
        jenkinsJob.setName(name);
        jenkinsJob.setUrl("http://somejenkins/jobs/" + name);
        return jenkinsJob;
    }

    @Test
    public void testProvideChecks() {

        final List<Check> checks = jenkinsJobListCheckProvider.provideChecks();
        assertEquals(3, checks.size());

        assertThat(checkNames(checks), containsInAnyOrder("docker-compose", "yana-contentmachine-build", "reco-svc-recommendations-build"));
    }

    @Test
    public void testWithJobPrefix() {

        final List<Check> checks = jenkinsJobListCheckProvider.withJobPrefix("yana-").provideChecks();
        assertEquals(1, checks.size());

        assertThat(checkNames(checks), containsInAnyOrder("contentmachine-build"));
    }

    @Test
    public void testWithJobNameTeamMapping() {

        final List<Check> checks = jenkinsJobListCheckProvider.withJobNameTeamMapping("reco-", mock(Team.class)).provideChecks();
        assertEquals(3, checks.size());

        assertThat(checkNames(checks), containsInAnyOrder("docker-compose", "yana-contentmachine-build", "svc-recommendations-build"));
        // test for setting team correctly can be found in testWithJobNameTeamMappingAndPrefix
    }

    @Test
    public void testWithJobNameTeamMappingAndPrefix() {

        final Team team = new Team() {
            @Override
            public String getTeamName() {
                return "someTeam";
            }

            @Override
            public String getJiraTeamName() {
                return "someTeamsJiraTeamName";
            }
        };

        final List<Check> checks = jenkinsJobListCheckProvider.withJobPrefix("yana-").withJobNameTeamMapping("contentmachine-", team).provideChecks();
        assertEquals(1, checks.size());

        final Check check = checks.get(0);

        assertEquals("build", check.getName());
        assertEquals(team, check.getTeam());
    }

    private List<String> checkNames(List<Check> checks) {
        return checks.stream().map(Check::getName).collect(Collectors.toList());
    }
}