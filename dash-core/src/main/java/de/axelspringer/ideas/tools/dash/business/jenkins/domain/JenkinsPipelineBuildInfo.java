package de.axelspringer.ideas.tools.dash.business.jenkins.domain;

import java.util.List;

public class JenkinsPipelineBuildInfo {

    private JenkinsPipelineStageResult result;

    private List<PipelineStage> stages;

    public JenkinsPipelineBuildInfo() {

    }

    public JenkinsPipelineBuildInfo(JenkinsPipelineStageResult result, List<PipelineStage> stages) {
        this.result = result;
        this.stages = stages;
    }

    public JenkinsPipelineStageResult getResult() {
        return result;
    }

    public void setResult(JenkinsPipelineStageResult result) {
        this.result = result;
    }

    public List<PipelineStage> getStages() {
        return stages;
    }

    public void setStages(List<PipelineStage> stages) {
        this.stages = stages;
    }
}
