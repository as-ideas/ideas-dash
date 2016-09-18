package de.axelspringer.ideas.tools.dash.business.jira;

import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.business.customization.Team;

import java.util.List;

public class JiraCheck extends Check {

    private static final String ICON_SRC = "assets/jira-logo.png";

    private final String url;

    private final String userName;

    private final String password;

    private final String jql;

    private JiraProjectConfiguration jiraProjectConfiguration = new JiraProjectConfiguration();

    public JiraCheck(String name, List<Team> teams, String url, String userName, String password, String jql, Group group) {
        super(name, group, teams);
        this.url = url;
        this.userName = userName;
        this.password = password;
        this.jql = jql;
    }

    public JiraCheck withJiraConfiguration(JiraProjectConfiguration jiraProjectConfiguration) {
        this.jiraProjectConfiguration = jiraProjectConfiguration;
        return this;
    }

    public String getUrl() {
        return this.url;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getPassword() {
        return this.password;
    }

    public String getJql() {
        return this.jql;
    }

    @Override
    public String getIconSrc() {
        return ICON_SRC;
    }

    public JiraProjectConfiguration getJiraProjectConfiguration() {
        return this.jiraProjectConfiguration;
    }
}
