package de.axelspringer.ideas.tools.dash.business.jenkins.job;

import java.util.List;

import de.axelspringer.ideas.tools.dash.business.check.AbstractCheck;
import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.business.customization.Team;
import de.axelspringer.ideas.tools.dash.business.jenkins.joblist.JenkinsJobNameMapper;
import de.axelspringer.ideas.tools.dash.business.jenkins.JenkinsServerConfiguration;

public class JenkinsJobCheck extends AbstractCheck {

    private static final String ICON_SRC = "assets/jenkins-logo.png";

    private final JenkinsServerConfiguration serverConfiguration;

    private final JenkinsJobNameMapper jenkinsJobNameMapper;

    private final String jobUrl;

    /**
     * Deprecated. Use {@link #JenkinsJobCheck(String, JenkinsServerConfiguration, String, Group, List, JenkinsJobNameMapper)} instead.
     */
    @Deprecated
    public JenkinsJobCheck(String name, String url, String userName, String apiToken, Group group, List<Team> teams, JenkinsJobNameMapper jenkinsJobNameMapper, String jobUrl) {
        super(name, group, teams);
        this.jobUrl = jobUrl;
        this.serverConfiguration = new JenkinsServerConfiguration(url, userName, apiToken);
        this.jenkinsJobNameMapper = jenkinsJobNameMapper;
    }

    public JenkinsJobCheck(String name, JenkinsServerConfiguration serverConfig, String jobUrl, Group group, List<Team> teams, JenkinsJobNameMapper jenkinsJobNameMapper) {
        super(name, group, teams);
        this.serverConfiguration = serverConfig;
        this.jenkinsJobNameMapper = jenkinsJobNameMapper;
        this.jobUrl = jobUrl;
    }

    public JenkinsJobNameMapper getJenkinsJobNameMapper() {
        return jenkinsJobNameMapper;
    }

    public JenkinsServerConfiguration getServerConfiguration() {
        return serverConfiguration;
    }

    public String getJobUrl() {
        return jobUrl;
    }

    @Override
    public String getIconSrc() {
        return ICON_SRC;
    }
}
