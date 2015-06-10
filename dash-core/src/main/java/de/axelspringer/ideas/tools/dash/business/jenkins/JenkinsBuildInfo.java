package de.axelspringer.ideas.tools.dash.business.jenkins;

import java.util.List;

public class JenkinsBuildInfo {

    private JenkinsResult result;
    private List<Action> actions;
    private boolean building;

    public JenkinsBuildInfo() {
    }

    public JenkinsResult getResult() {
        return this.result;
    }

    public List<Action> getActions() {
        return this.actions;
    }

    public boolean isBuilding() {
        return this.building;
    }

    public void setResult(JenkinsResult result) {
        this.result = result;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public void setBuilding(boolean building) {
        this.building = building;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof JenkinsBuildInfo)) {
            return false;
        }
        final JenkinsBuildInfo other = (JenkinsBuildInfo) o;
        if (!other.canEqual((Object) this)) {
            return false;
        }
        final Object this$result = this.result;
        final Object other$result = other.result;
        if (this$result == null ? other$result != null : !this$result.equals(other$result)) {
            return false;
        }
        final Object this$actions = this.actions;
        final Object other$actions = other.actions;
        if (this$actions == null ? other$actions != null : !this$actions.equals(other$actions)) {
            return false;
        }
        if (this.building != other.building) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $result = this.result;
        result = result * PRIME + ($result == null ? 0 : $result.hashCode());
        final Object $actions = this.actions;
        result = result * PRIME + ($actions == null ? 0 : $actions.hashCode());
        result = result * PRIME + (this.building ? 79 : 97);
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof JenkinsBuildInfo;
    }

    public String toString() {
        return "de.axelspringer.ideas.tools.dash.business.jenkins.JenkinsBuildInfo(result=" + this.result + ", actions=" + this.actions + ", building=" + this.building + ")";
    }

    public enum JenkinsResult {SUCCESS, UNSTABLE, ABORTED, FAILURE}

    public class Action {
        private Integer failCount;
        private Integer totalCount;

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
    }
}
