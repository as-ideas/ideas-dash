package de.axelspringer.ideas.tools.dash.business.jenkins.domain;

public class PipelineStage {

    private Integer id;
    private String name;
    private JenkinsPipelineStageResult status;
    private long durationMillis;

    public PipelineStage() {
    }

    public PipelineStage(String name) {
        this.name = name;
    }

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

    public JenkinsPipelineStageResult getStatus() {
        return status;
    }

    public void setStatus(JenkinsPipelineStageResult status) {
        this.status = status;
    }

    public long getDurationMillis() {
        return durationMillis;
    }

    public void setDurationMillis(long durationMillis) {
        this.durationMillis = durationMillis;
    }
}