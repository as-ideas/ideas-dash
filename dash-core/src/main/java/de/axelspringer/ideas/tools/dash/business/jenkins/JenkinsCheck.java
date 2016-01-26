package de.axelspringer.ideas.tools.dash.business.jenkins;

import java.util.List;

import de.axelspringer.ideas.tools.dash.business.check.AbstractCheck;
import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.business.customization.Team;

public class JenkinsCheck extends AbstractCheck {

    private static final String ICON_SRC = "assets/jenkins-logo.png";

    private final String url;

    private final String userName;

    private final String apiToken;

    private final JenkinsJobNameMapper jenkinsJobNameMapper;

    public JenkinsCheck(String name, String url, String userName, String apiToken, Group group, List<Team> teams, JenkinsJobNameMapper jenkinsJobNameMapper) {
        super(name, group, teams);
        this.url = url;
        this.userName = userName;
        this.apiToken = apiToken;
        this.jenkinsJobNameMapper = jenkinsJobNameMapper;
    }

    public String getUrl() {
        return url;
    }

    public String getUserName() {
        return userName;
    }

    public String getApiToken() {
        return apiToken;
    }

    public JenkinsJobNameMapper getJenkinsJobNameMapper() {
        return jenkinsJobNameMapper;
    }

    @Override
    public String getIconSrc() {
        return ICON_SRC;
    }
}
