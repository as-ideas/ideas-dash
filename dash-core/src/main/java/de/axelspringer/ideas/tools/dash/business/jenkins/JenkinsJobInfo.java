package de.axelspringer.ideas.tools.dash.business.jenkins;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class JenkinsJobInfo {

    private LastBuild lastCompletedBuild;

    private LastBuild lastBuild;

    public JenkinsJobInfo() {
    }

    public LastBuild getLastCompletedBuild() {
        return this.lastCompletedBuild;
    }

    public LastBuild getLastBuild() {
        return this.lastBuild;
    }

    public void setLastCompletedBuild(LastBuild lastCompletedBuild) {
        this.lastCompletedBuild = lastCompletedBuild;
    }

    public void setLastBuild(LastBuild lastBuild) {
        this.lastBuild = lastBuild;
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

    public class LastBuild {

        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
