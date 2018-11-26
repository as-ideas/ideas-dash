package de.axelspringer.ideas.tools.dash.business.jenkins.domain;

public class Build {

    private String url;

    private long timestamp;

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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
