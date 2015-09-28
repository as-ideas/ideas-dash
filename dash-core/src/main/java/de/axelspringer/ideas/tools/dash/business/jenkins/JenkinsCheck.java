package de.axelspringer.ideas.tools.dash.business.jenkins;

import de.axelspringer.ideas.tools.dash.business.check.AbstractCheck;
import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.business.customization.Team;

public class JenkinsCheck extends AbstractCheck {

    private static final String ICON_SRC = "assets/jenkins-logo.png";

    private final String url;

    private final String userName;

    private final String apiToken;

    public JenkinsCheck(String name, String url, String userName, String apiToken, Group group, Team team) {
        super(name, group, team);
        this.url = url;
        this.userName = userName;
        this.apiToken = apiToken;
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

    @Override
    public String getIconSrc() {
        return ICON_SRC;
    }
}
