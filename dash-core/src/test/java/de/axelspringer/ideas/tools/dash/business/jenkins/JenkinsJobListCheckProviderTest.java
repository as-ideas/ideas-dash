package de.axelspringer.ideas.tools.dash.business.jenkins;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.http.auth.AuthenticationException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.business.customization.Team;

public class JenkinsJobListCheckProviderTest {

    private JenkinsJobListCheckProvider jenkinsJobListCheckProvider;

    @Before
    public void initMocks() throws IOException, AuthenticationException {

        jenkinsJobListCheckProvider = new JenkinsJobListCheckProvider("fooHost", "fooUser", "fooApiToken", mock(Group.class));

        final List<JenkinsJob> jobs = new ArrayList<>();
        jobs.add(jenkinsJob("docker-compose", true));
        jobs.add(jenkinsJob("yana-contentmachine-build", true));
        jobs.add(jenkinsJob("yana-contentmachine-disabled", false));
        jobs.add(jenkinsJob("reco-svc-recommendations-build", true));
        final JenkinsJobListWrapper jenkinsJobListWrapper = new JenkinsJobListWrapper();
        jenkinsJobListWrapper.setJobs(jobs);

        final JenkinsClient jenkinsClient = mock(JenkinsClient.class);
        when(jenkinsClient.query(anyString(), anyString(), anyString(), eq(JenkinsJobListWrapper.class))).thenReturn(jenkinsJobListWrapper);

        ReflectionTestUtils.setField(jenkinsJobListCheckProvider, "jenkinsClient", jenkinsClient);
    }

    private JenkinsJob jenkinsJob(String name, boolean enabled) {
        final JenkinsJob jenkinsJob = new JenkinsJob();
        jenkinsJob.setName(name);
        jenkinsJob.setUrl("http://somejenkins/jobs/" + name);
        final String color = enabled ? "blue" : "disabled";
        jenkinsJob.setColor(color);
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
    public void testWithJobNameTeamMappingAndPrefix() {

    	final Team team = createsFakeTeamWithGivenName("someTeam");

        final List<Check> checks = jenkinsJobListCheckProvider.withJobPrefix("yana-").withJobNameTeamMapping("contentmachine-", Arrays.asList(team)).provideChecks();
        assertEquals(1, checks.size());

        final Check check = checks.get(0);

        assertEquals("contentmachine-build", check.getName());
        assertFalse(check.getTeams().isEmpty());
        assertEquals(team, check.getTeams().get(0));
    }
    
    @Test
    public void testWithJobNameMultipleTeamsMappingAndPrefix() {

        final Team firstTeam = createsFakeTeamWithGivenName("someTeam");
        final Team secondTeam = createsFakeTeamWithGivenName("sameName2");

        final List<Check> checks = jenkinsJobListCheckProvider.withJobPrefix("yana-").withJobNameTeamMapping("contentmachine-", Arrays.asList(firstTeam, secondTeam)).provideChecks();
        assertEquals(1, checks.size());

        final Check check = checks.get(0);

        assertEquals("contentmachine-build", check.getName());
        assertFalse(check.getTeams().isEmpty());
        assertEquals(firstTeam, check.getTeams().get(0));
        assertEquals(secondTeam, check.getTeams().get(1));
    }
    
    private Team createsFakeTeamWithGivenName(final String teamName) {
    	return new Team() {
            @Override
            public String getTeamName() {
                return teamName;
            }

            @Override
            public String getJiraTeamName() {
                return "someTeamsJiraTeamName";
            }
        };
    }

    private List<String> checkNames(List<Check> checks) {
        return checks.stream().map(Check::getName).collect(Collectors.toList());
    }
}