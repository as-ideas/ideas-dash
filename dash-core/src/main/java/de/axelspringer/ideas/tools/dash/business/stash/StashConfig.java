package de.axelspringer.ideas.tools.dash.business.stash;

public class StashConfig {

    private final String stashServerUrl;
    private final String stashUserName;
    private final String stashUserPassword;

    public StashConfig(String stashServerUrl, String stashUserName, String stashUserPassword) {
        this.stashServerUrl = stashServerUrl;
        this.stashUserName = stashUserName;
        this.stashUserPassword = stashUserPassword;
    }

    public String stashServerUrl() {
        return stashServerUrl;
    }

    public String stashUserName() {
        return stashUserName;
    }

    public String stashUserPassword() {
        return stashUserPassword;
    }
}
