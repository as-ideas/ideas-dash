package de.axelspringer.ideas.tools.dash.business.jenkins.joblist;


import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.check.CheckProvider;
import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.business.customization.Team;
import de.axelspringer.ideas.tools.dash.business.jenkins.JenkinsCheck;
import de.axelspringer.ideas.tools.dash.business.jenkins.JenkinsClient;
import de.axelspringer.ideas.tools.dash.business.jenkins.JenkinsServerConfiguration;
import de.axelspringer.ideas.tools.dash.business.jenkins.domain.JenkinsElement;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Helper that will load a jenkins job list and
 */
public class JenkinsJobListCheckProvider implements CheckProvider {

    private static final String DISABLED_COLOR = "disabled";

    private static final Logger log =
            org.slf4j.LoggerFactory.getLogger(JenkinsJobListCheckProvider.class);

    private final JenkinsServerConfiguration serverConfig;

    /**
     * group that the checkresult will be added to
     */
    private final Group group;

    /**
     * jenkins job prefix. if set only jobs with this prefix will be included.
     */
    private String jobPrefix;

    /**
     * If set, is called for every job to check if this job should be included.
     */
    private Predicate<JenkinsElement> jobMatcher;

    /**
     * jobs with names containing a string represented in this list will be excluded
     */
    private List<String> blacklist = new ArrayList<>();

    /**
     * contains a mapping for jobNameSegment to team mapping
     */
    private Map<String, List<Team>> jobNameTeamMapping = new HashMap<>();

    /**
     * Is called for every job to identify the team the job should belong to
     */
    private Function<JenkinsElement, List<Team>> teamMapper;

    /**
     * Optional jenkins job name mapper
     */
    private JenkinsJobNameMapper jenkinsJobNameMapper;

    @Autowired
    private JenkinsClient jenkinsClient;

    /**
     * Deprecated, use {@link #JenkinsJobListCheckProvider(JenkinsServerConfiguration, Group)}
     * instead.
     *
     * @param host     {@link #serverConfig}
     * @param user     {@link #serverConfig}
     * @param apiToken {@link #serverConfig}
     * @param group    {@link #group}
     */
    @Deprecated
    public JenkinsJobListCheckProvider(String host, String user, String apiToken, Group group) {
        this.serverConfig = new JenkinsServerConfiguration(host, user, apiToken);
        this.group = group;
    }

    public JenkinsJobListCheckProvider(JenkinsServerConfiguration serverConfiguration,
                                       Group group) {
        this.serverConfig = serverConfiguration;
        this.group = group;
    }

    @Override
    public List<Check> provideChecks() {

        return jobs(serverConfig.getUrl() + "/api/json").stream()
                .filter(this::jobMatches)
                .filter(this::matchesPrefix)
                .filter(this::isEnabled)
                .filter(this::isNotBlacklisted)
                .map(this::check)
                .collect(Collectors.toList());
    }

    private boolean isNotBlacklisted(JenkinsElement jenkinsJob) {

        for (String blacklistEntry : blacklist) {
            if (jenkinsJob.getName().toLowerCase().contains(blacklistEntry.toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    public void setJenkinsJobNameMapper(JenkinsJobNameMapper jenkinsJobNameMapper) {
        this.jenkinsJobNameMapper = jenkinsJobNameMapper;
    }

    private Check check(JenkinsElement job) {

        final String jobName =
                StringUtils.isBlank(jobPrefix) || !job.getName().startsWith(jobPrefix) ?
                        job.getName() :
                        job.getName().substring(jobPrefix.length());

        final List<Team> teams = new ArrayList<>();

        jobNameTeamMapping.forEach((jobIdentifactionName, mappedTeams) -> {
            if (jobName.contains(jobIdentifactionName)) {
                teams.addAll(mappedTeams);
            }
        });

        if (teamMapper != null) {
            teams.addAll(teamMapper.apply(job));
        }

        return new JenkinsCheck(jobName, job.getUrl(), serverConfig, group, teams)
                .withJobNameMapper(jenkinsJobNameMapper)
                .withExplodePipelines(false);
    }

    private List<JenkinsElement> jobs(String url) {

        List<JenkinsElement> elements =
                jenkinsClient.queryApi(url, serverConfig, JenkinsJobListWrapper.class).getJobs();

        return Optional.ofNullable(elements)
                .orElse(new ArrayList<>())
                .stream()
                .flatMap(
                        element -> isJob(element) ? Stream.of(element) : jobs(element.getUrl()).stream()
                )
                .collect(Collectors.toList());
    }

    private boolean isFolder(JenkinsElement element) {

        final String elementType = element.getElementType();
        return JenkinsElement.ELEMENT_TYPE_ORGANIZATIONAL_FOLDER.equals(elementType)
                || JenkinsElement.ELEMENT_TYPE_FOLDER.equals(elementType)
                || JenkinsElement.ELEMENT_TYPE_WORKFLOW_MULTI_BRANCH_PROJECT.equals(elementType);
    }

    private boolean isJob(JenkinsElement element) {

        // for now dont explicitly list all job elements (as not all classes are registered) but assume everything that is not a folder is a job
        return !isFolder(element);
    }

    private boolean matchesPrefix(JenkinsElement job) {

        return StringUtils.isBlank(jobPrefix) || job.getName().startsWith(jobPrefix);
    }

    private boolean jobMatches(JenkinsElement jenkinsJob) {
        return this.jobMatcher == null || jobMatcher.test(jenkinsJob);
    }

    private boolean isEnabled(JenkinsElement job) {

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

    public JenkinsJobListCheckProvider withJobMatcher(Predicate<JenkinsElement> predicate) {
        this.jobMatcher = predicate;
        return this;
    }

    public JenkinsJobListCheckProvider withTeamMapper(Function<JenkinsElement, List<Team>> mapper) {
        this.teamMapper = mapper;
        return this;
    }

    /**
     * @param jobIdentificationName job identification in the job name
     * @param teams                 teams that jobs prefixed with given prefix will be mapped to
     * @return this {@link JenkinsJobListCheckProvider} instance
     */
    public JenkinsJobListCheckProvider withJobNameTeamMapping(String jobIdentificationName,
                                                              List<Team> teams) {
        jobNameTeamMapping.put(jobIdentificationName, teams);
        return this;
    }

    /**
     * see {@link #blacklist}
     */
    public JenkinsJobListCheckProvider withBlacklistedName(String namePart) {
        blacklist.add(namePart);
        return this;
    }
}
