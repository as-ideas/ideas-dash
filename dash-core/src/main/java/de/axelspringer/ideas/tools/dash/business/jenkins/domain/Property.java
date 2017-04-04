package de.axelspringer.ideas.tools.dash.business.jenkins.domain;

import com.google.gson.annotations.SerializedName;

public class Property {

    public final static String MULTIBRANCH_CLASS = "org.jenkinsci.plugins.workflow.multibranch.BranchJobProperty";

    @SerializedName("_class")
    private String propertyClass;

    public Property() {
    }

    public Property(String propertyClass) {
        this.propertyClass = propertyClass;
    }

    public String getPropertyClass() {
        return propertyClass;
    }

    public void setPropertyClass(String propertyClass) {
        this.propertyClass = propertyClass;
    }
}
