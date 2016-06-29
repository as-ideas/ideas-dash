package de.axelspringer.ideas.tools.dash.business.jenkins;

public class JenkinsServerConfiguration {

    private final String url;

    private final String userName;

    private final String apiToken;

    public JenkinsServerConfiguration(String url, String userName, String apiToken) {
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
}
