package de.axelspringer.ideas.tools.dash.business.jenkins.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JenkinsBuildAction {

    public final static String PARAMETERS_ACTION = "hudson.model.ParametersAction";

    @SerializedName("_class")
    private String actionClass;

    private Integer failCount;
    private Integer totalCount;

    private List<JenkinsParameter> parameters;

    public Integer getFailCount() {
        return failCount;
    }

    public void setFailCount(Integer failCount) {
        this.failCount = failCount;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<JenkinsParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<JenkinsParameter> parameters) {
        this.parameters = parameters;
    }

    public String getActionClass() {
        return actionClass;
    }

    public void setActionClass(String actionClass) {
        this.actionClass = actionClass;
    }

    public class JenkinsParameter {
        private String name;
        private String value;

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
