package de.axelspringer.ideas.tools.dash.business.jenkins.domain;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class JenkinsJobInfo {

    public final static String PIPELINE_CLASS = "org.jenkinsci.plugins.workflow.job.WorkflowJob";

    @SerializedName("_class")
    private String buildClass;

    private Build lastCompletedBuild;

    private Build lastBuild;

    private Build lastSuccessfulBuild;

    private Boolean buildable;

    public JenkinsJobInfo() {
    }

    public JenkinsJobInfo(Build lastSuccessfulBuild, Build lastBuild) {
        this.lastSuccessfulBuild = lastSuccessfulBuild;
        this.lastBuild = lastBuild;
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

    public void setBuildClass(String buildClass) {
        this.buildClass = buildClass;
    }

    public Boolean isBuildable() {
        return buildable;
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
