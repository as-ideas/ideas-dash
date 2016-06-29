package de.axelspringer.ideas.tools.dash.business.jenkins.pipeline;

import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.check.CheckExecutor;
import de.axelspringer.ideas.tools.dash.business.check.CheckResult;
import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.business.jenkins.JenkinsClient;
import de.axelspringer.ideas.tools.dash.business.jenkins.JenkinsServerConfiguration;
import de.axelspringer.ideas.tools.dash.business.jenkins.domain.JenkinsJobInfo;
import de.axelspringer.ideas.tools.dash.presentation.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JenkinsPipelineCheckExecutor implements CheckExecutor<JenkinsPipelineCheck> {

    @Autowired
    private JenkinsClient jenkinsClient;

    @Override
    public List<CheckResult> executeCheck(JenkinsPipelineCheck check) {

        final JenkinsServerConfiguration serverConfig = check.getServerConfiguration();

        // get all info about the job
        final JenkinsJobInfo jobInfo = jenkinsClient.queryApi(check.getJobUrl(), serverConfig, JenkinsJobInfo.class);

        // try and fetch info from last successful build
        final List<JenkinsPipelineBuildInfo.PipelineStage> lastSuccessfulBuildStages = buildStages(serverConfig, jobInfo.getLastSuccessfulBuild());

        // fetch info from last build
        final List<JenkinsPipelineBuildInfo.PipelineStage> lastBuildStages = buildStages(serverConfig, jobInfo.getLastBuild());

        // make sure we have something to work with
        if (lastSuccessfulBuildStages.size() < 1 && lastBuildStages.size() < 1) {
            return Collections.singletonList(new CheckResult(State.GREY, check.getName(), "no info available", 0, 0, check.getGroup()));
        }

        // if the last build contains any stage that is not represented in the last successful build we need to use the last build as definition as the pipe def changed
        boolean lastBuildAsTemplate = lastBuildStages.stream().anyMatch(
                lastBuildStage -> !containsStageWithName(lastBuildStage.getName(), lastSuccessfulBuildStages)
        );
        final List<JenkinsPipelineBuildInfo.PipelineStage> pipeDefinition = lastBuildAsTemplate ? lastBuildStages : lastSuccessfulBuildStages;

        return pipeDefinition.stream()
                .map(stage -> checkResult(stage, lastBuildStages, check.getGroup()))
                .collect(Collectors.toList());
    }

    private boolean containsStageWithName(String name, List<JenkinsPipelineBuildInfo.PipelineStage> stages) {
        return stages.stream().anyMatch(stage -> stage.getName().equals(name));
    }

    private CheckResult checkResult(JenkinsPipelineBuildInfo.PipelineStage stageDefinition, List<JenkinsPipelineBuildInfo.PipelineStage> buildStages, Group group) {

        final Optional<JenkinsPipelineBuildInfo.PipelineStage> executedStage = buildStages.stream()
                .filter(stage -> stage.getName().equals(stageDefinition.getName()))
                .findAny();

        if (executedStage.isPresent()) {
            final JenkinsPipelineBuildInfo.PipelineStage stage = executedStage.get();
            return new CheckResult(state(stage), stage.getName(), info(stage), 1, failCount(stage), group);
        }
        return new CheckResult(State.GREY, stageDefinition.getName(), "NOT EXECUTED", 0, 0, group);
    }

    private Integer failCount(JenkinsPipelineBuildInfo.PipelineStage stage) {
        return state(stage) == State.RED ? 1 : 0;
    }

    private String info(JenkinsPipelineBuildInfo.PipelineStage stage) {
        return stage.getDurationMillis() / 1000 + " seconds";
    }

    private State state(JenkinsPipelineBuildInfo.PipelineStage stage) {

        switch (stage.getStatus()) {
            case SUCCESS:
                return State.GREEN;
            case ABORTED:
                return State.GREY;
            default:
                return State.RED;
        }
    }

    private List<JenkinsPipelineBuildInfo.PipelineStage> buildStages(JenkinsServerConfiguration serverConfig, JenkinsJobInfo.Build build) {

        if (build == null) {
            return Collections.emptyList();
        }

        final JenkinsPipelineBuildInfo buildInfo = jenkinsClient.queryWorkflowApi(build.getUrl(), serverConfig, JenkinsPipelineBuildInfo.class);
        return buildInfo.getStages() != null ? buildInfo.getStages() : Collections.emptyList();
    }

    @Override
    public boolean isApplicable(Check check) {
        return check instanceof JenkinsPipelineCheck;
    }
}
