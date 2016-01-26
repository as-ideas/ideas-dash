package de.axelspringer.ideas.tools.dash.business.jenkins;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.check.CheckProvider;
import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.business.customization.Team;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.auth.AuthenticationException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Helper that will load a jenkins job list and
 */
public class JenkinsJobListCheckProvider implements CheckProvider {

    private static final String DISABLED_COLOR = "disabled";
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(JenkinsJobListCheckProvider.class);

    /**
     * jenkins host adress
     */
    private final String host;

    /**
     * jenkins username
     */
    private final String user;

    /**
     * jenkins api token
     */
    private final String apiToken;

    /**
     * group that the checkresult will be added to
     */
    private final Group group;

    /**
     * jenkins job prefix. if set only jobs with this prefix will be included.
     */
    private String jobPrefix;

    /**
     * contains a mapping for jobNameSegment to team mapping
     */
    private Map<String, List<Team>> jobNameTeamMapping = new HashMap<>();

    @Autowired
    private JenkinsClient jenkinsClient;

    /**
     * @param host     {@link #host}
     * @param user     {@link #user}
     * @param apiToken {@link #apiToken}
     * @param group    {@link #group}
     */
    public JenkinsJobListCheckProvider(String host, String user, String apiToken, Group group) {
        this.host = host;
        this.user = user;
        this.apiToken = apiToken;
        this.group = group;
    }

    @Override
    public List<Check> provideChecks() {

        return jobs().stream()
                .filter(this::matchesPrefix)
                .filter(this::isEnabled)
                .map(this::check)
                .collect(Collectors.toList());
    }

    private Check check(JenkinsJob job) {

        final String jobName = StringUtils.isBlank(jobPrefix) || !job.getName().startsWith(jobPrefix) ? job.getName() : job.getName().substring(jobPrefix.length());

        List<Team> teams = null;
        for (String jobNameTeamPrefix : jobNameTeamMapping.keySet()) {
            if (jobName.startsWith(jobNameTeamPrefix)) {
                teams = jobNameTeamMapping.get(jobNameTeamPrefix);
            }
        }

        return new JenkinsCheck(jobName, job.getUrl(), user, apiToken, group, teams);
    }

    private List<JenkinsJob> jobs() {

        final String url = host + "/api/json";
        try {
            return jenkinsClient.query(url, user, apiToken, JenkinsJobListWrapper.class).getJobs();
        } catch (IOException | AuthenticationException e) {
            // does this need further processing? fix it if it fails :D
            log.error("could not load job list from jenkins. URL=" + url);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private boolean matchesPrefix(JenkinsJob job) {

        return StringUtils.isBlank(jobPrefix) || job.getName().startsWith(jobPrefix);
    }

    private boolean isEnabled(JenkinsJob job) {

        return !DISABLED_COLOR.equals(job.getColor());
    }

    /**
     * @param jobPrefix {@link #jobPrefix}
     * @return this {@link JenkinsJobListCheckProvider} instance
     */
    public JenkinsJobListCheckProvider withJobPrefix(String jobPrefix) {
        this.jobPrefix = jobPrefix;
        return this;
    }

    /**
     * @param jobNameTeamPrefix team prefix in the job name
     * @param teams             teams that jobs prefixed with given prefix will be mapped to
     * @return this {@link JenkinsJobListCheckProvider} instance
     */
    public JenkinsJobListCheckProvider withJobNameTeamMapping(String jobNameTeamPrefix, List<Team> teams) {
        jobNameTeamMapping.put(jobNameTeamPrefix, teams);
        return this;
    }
}
