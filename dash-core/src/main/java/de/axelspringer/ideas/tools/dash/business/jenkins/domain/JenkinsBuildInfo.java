package de.axelspringer.ideas.tools.dash.business.jenkins.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class JenkinsBuildInfo {

    private JenkinsResult result;
    private List<JenkinsBuildAction> actions;
    private boolean building;

    public JenkinsBuildInfo() {
    }

    public JenkinsResult getResult() {
        return this.result;
    }

    public List<JenkinsBuildAction> getActions() {
        return this.actions;
    }

    public boolean isBuilding() {
        return this.building;
    }

    public void setResult(JenkinsResult result) {
        this.result = result;
    }

    public void setActions(List<JenkinsBuildAction> actions) {
        this.actions = actions;
    }

    public void setBuilding(boolean building) {
        this.building = building;
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
}
