package de.axelspringer.ideas.tools.dash.business.github;

import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.business.customization.Team;

import java.util.List;

public class GithubCheck extends Check {

    private final GithubConfig githubConfig;

    private String regexForMatchingRepoNames = "";

    /**
     * if specified the pr results will be filtered down to prs containing this keyword
     */
    private String filterKeyword;

    // eg. orgs/as-ideas or user/waschnick
    private String githubFullyQualifiedName;

    public GithubCheck(String name, Group group, List<Team> teams, GithubConfig githubConfig, String githubFullyQualifiedName, String regexForMatchingRepoNames) {
        super(name, group, teams);
        this.githubConfig = githubConfig;
        this.regexForMatchingRepoNames = regexForMatchingRepoNames;
        this.githubFullyQualifiedName = githubFullyQualifiedName;
    }

    public GithubConfig githubConfig() {
        return githubConfig;
    }

    public String regexForMatchingRepoNames() {
        return regexForMatchingRepoNames;
    }

    public String githubFullyQualifiedName() {
        return githubFullyQualifiedName;
    }

    public GithubCheck withFilterKeyword(String filterKeyword) {
        this.filterKeyword = filterKeyword;
        return this;
    }

    public String getFilterKeyword() {
        return filterKeyword;
    }

    @Override
    public String getIconSrc() {
        return "assets/github-logo.png";
    }
}
