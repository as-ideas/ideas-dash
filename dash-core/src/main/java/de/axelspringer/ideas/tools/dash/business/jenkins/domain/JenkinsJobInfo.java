package de.axelspringer.ideas.tools.dash.business.jenkins.domain;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class JenkinsJobInfo {

    public final static String PIPELINE_CLASS = "org.jenkinsci.plugins.workflow.job.WorkflowJob";

    @SerializedName("_class")
    private String buildClass;

    private Build lastCompletedBuild;

    private Build lastBuild;

    private Build lastSuccessfulBuild;

    private Boolean buildable;

    private String name;

    @SerializedName("property")
    private List<Property> properties;

    public JenkinsJobInfo() {
    }

    public JenkinsJobInfo(Build lastSuccessfulBuild, Build lastBuild) {
        this.lastSuccessfulBuild = lastSuccessfulBuild;
        this.lastBuild = lastBuild;
    }

    public JenkinsJobInfo(String buildClass, Build lastCompletedBuild, Build lastBuild, Build lastSuccessfulBuild, Boolean buildable, String name, List<Property> properties) {
        this.buildClass = buildClass;
        this.lastCompletedBuild = lastCompletedBuild;
        this.lastBuild = lastBuild;
        this.lastSuccessfulBuild = lastSuccessfulBuild;
        this.buildable = buildable;
        this.name = name;
        this.properties = properties;
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

    public String getBuildClass() {
        return buildClass;
    }

    public void setLastCompletedBuild(Build lastCompletedBuild) {
        this.lastCompletedBuild = lastCompletedBuild;
    }

    public void setLastBuild(Build lastBuild) {
        this.lastBuild = lastBuild;
    }

    public void setLastSuccessfulBuild(Build lastSuccessfulBuild) {
        this.lastSuccessfulBuild = lastSuccessfulBuild;
    }

    public Boolean getBuildable() {
        return buildable;
    }

    public void setBuildable(Boolean buildable) {
        this.buildable = buildable;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public void setBuildClass(String buildClass) {
        this.buildClass = buildClass;
    }

    public Boolean isBuildable() {
        return buildable != null && buildable;
    }

    public Boolean isPipeline() {
        return PIPELINE_CLASS.equals(buildClass);
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
