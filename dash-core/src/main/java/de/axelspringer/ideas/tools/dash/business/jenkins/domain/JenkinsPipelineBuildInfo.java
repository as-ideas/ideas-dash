package de.axelspringer.ideas.tools.dash.business.jenkins.domain;

import java.util.List;

public class JenkinsPipelineBuildInfo {

    private JenkinsResult result;

    private List<PipelineStage> stages;

    public JenkinsPipelineBuildInfo() {

    }

    public JenkinsResult getResult() {
        return result;
    }

    public void setResult(JenkinsResult result) {
        this.result = result;
    }

    public List<PipelineStage> getStages() {
        return stages;
    }

    public void setStages(List<PipelineStage> stages) {
        this.stages = stages;
    }

    public class PipelineStage {

        private Integer id;
        private String name;
        private JenkinsResult status;
        private long durationMillis;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public JenkinsResult getStatus() {
            return status;
        }

        public void setStatus(JenkinsResult status) {
            this.status = status;
        }

        public long getDurationMillis() {
            return durationMillis;
        }

        public void setDurationMillis(long durationMillis) {
            this.durationMillis = durationMillis;
        }
    }
}
