package de.axelspringer.ideas.tools.dash.business.jira;

import de.axelspringer.ideas.tools.dash.business.check.AbstractCheck;
import de.axelspringer.ideas.tools.dash.business.customization.Stage;
import de.axelspringer.ideas.tools.dash.business.customization.Team;

public class JiraCheck extends AbstractCheck {

    private final String url;

    private final String userName;

    private final String password;

    private final String jql;

    public JiraCheck(String name, Team team, String url, String userName, String password, String jql, Stage group) {
        super(name, group, team);
        this.url = url;
        this.userName = userName;
        this.password = password;
        this.jql = jql;
    }

    public String getUrl() {
        return url;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getJql() {
        return jql;
    }
}
