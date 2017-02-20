package de.axelspringer.ideas.tools.dash.business.jenkins.domain;

import com.google.gson.annotations.SerializedName;

public class JenkinsElement {

    public final static String ELEMENT_TYPE_ORGANIZATIONAL_FOLDER = "jenkins.branch.OrganizationFolder";
    public final static String ELEMENT_TYPE_FOLDER = "com.cloudbees.hudson.plugins.folder.Folder";
    public final static String ELEMENT_TYPE_WORKFLOW_JOB = "org.jenkinsci.plugins.workflow.job.WorkflowJob";
    public static final String ELEMENT_TYPE_WORKFLOW_MULTI_BRANCH_PROJECT = "org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject";
    public static final String ELEMENT_TYPE_FREESTYLE_PROJECT = "hudson.model.FreeStyleProject";

    @SerializedName("_class")
    private String elementType;

    private String name;

    private String url;

    private String color;

    public String getElementType() {
        return elementType;
    }

    public void setElementType(String elementType) {
        this.elementType = elementType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    private boolean isFolder() {

        return ELEMENT_TYPE_ORGANIZATIONAL_FOLDER.equals(elementType)
                || ELEMENT_TYPE_FOLDER.equals(elementType)
                || ELEMENT_TYPE_WORKFLOW_MULTI_BRANCH_PROJECT.equals(elementType);
    }

    public boolean isJob() {

        // for now dont explicitly list all job elements (as not all classes are registered) but assume everything that is not a folder is a job
        return !isFolder();
    }
}
