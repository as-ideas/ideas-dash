package de.axelspringer.ideas.tools.dash.business.jenkins.domain;

public class Build {

    private String url;

    public Build() {

    }

    public Build(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
