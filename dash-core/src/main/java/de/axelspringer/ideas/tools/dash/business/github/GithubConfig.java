package de.axelspringer.ideas.tools.dash.business.github;

public class GithubConfig {

    private final String githubUserName;
    private final String githubpersonalApiToken;

    public GithubConfig( String githubUserName, String githubpersonalApiToken) {
        this.githubUserName = githubUserName;
        this.githubpersonalApiToken = githubpersonalApiToken;
    }

    public String githubUserName() {
        return githubUserName;
    }

    public String githubUserPassword() {
        return githubpersonalApiToken;
    }
}
