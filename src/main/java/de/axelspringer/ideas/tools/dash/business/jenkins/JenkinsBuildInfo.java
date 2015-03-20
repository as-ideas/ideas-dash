package de.axelspringer.ideas.tools.dash.business.jenkins;

import lombok.Data;

import java.util.List;

@Data
public class JenkinsBuildInfo {

    private JenkinsResult result;
    private List<Action> actions;
    private boolean building;

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
