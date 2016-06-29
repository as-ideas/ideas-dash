package de.axelspringer.ideas.tools.dash.business.jenkins.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class JenkinsJobInfo {

    private Build lastCompletedBuild;

    private Build lastBuild;

    private Build lastSuccessfulBuild;

    private Boolean buildable;

    public JenkinsJobInfo() {
    }

    public Build getLastSuccessfulBuild() {
        return lastSuccessfulBuild;
    }

    public Build getLastCompletedBuild() {
        return this.lastCompletedBuild;
    }

    public Build getLastBuild() {
        return this.lastBuild;
    }

    public Boolean isBuildable() {
        return buildable;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public class Build {

        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
