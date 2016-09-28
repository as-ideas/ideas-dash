package de.axelspringer.ideas.tools.dash.business.jenkins.executor;

import de.axelspringer.ideas.tools.dash.business.check.checkresult.CheckResult;
import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.business.jenkins.JenkinsCheck;
import de.axelspringer.ideas.tools.dash.business.jenkins.JenkinsClient;
import de.axelspringer.ideas.tools.dash.business.jenkins.JenkinsServerConfiguration;
import de.axelspringer.ideas.tools.dash.business.jenkins.domain.*;
import de.axelspringer.ideas.tools.dash.presentation.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static de.axelspringer.ideas.tools.dash.business.jenkins.domain.JenkinsPipelineStageResult.IN_PROGRESS;

@Service
public class JenkinsPipelineExecutor {

    private final JenkinsClient jenkinsClient;

    @Autowired
    public JenkinsPipelineExecutor(JenkinsClient jenkinsClient) {
        this.jenkinsClient = jenkinsClient;
    }

    public List<CheckResult> executeCheck(JenkinsJobInfo jobInfo, JenkinsCheck check, BuildInfo buildInfo) {

        final JenkinsServerConfiguration serverConfig = check.getServerConfiguration();

        // try and fetch info from last successful build
        final List<PipelineStage> lastSuccessfulBuildStages = buildStages(serverConfig, jobInfo.getLastSuccessfulBuild());

        // fetch info from last build
        final Build lastBuild = jobInfo.getLastBuild();
        final List<PipelineStage> lastBuildStages = buildStages(serverConfig, lastBuild);

        // make sure we have something to work with
        if (lastSuccessfulBuildStages.size() < 1 && lastBuildStages.size() < 1) {
            return Collections.singletonList(
                    new CheckResult(State.GREY, check.getName(), "no info available", 0, 0, check.getGroup())
                            .withLink(check.getJobUrl())
            );
        }

        // if the last build contains any stage that is not represented in the last successful build we need to use the last build as definition as the pipe def changed
        boolean lastBuildAsTemplate = lastBuildStages.stream().anyMatch(
                lastBuildStage -> !containsStageWithName(lastBuildStage.getName(), lastSuccessfulBuildStages)
        );
        final List<PipelineStage> pipeDefinition = lastBuildAsTemplate ? lastBuildStages : lastSuccessfulBuildStages;

        final List<CheckResult> checkResults = new ArrayList<>();

        //add no-op check results for parameters
        jenkinsClient.buildParameters(lastBuild.getUrl(), serverConfig)
                .forEach((name, value) -> checkResults.add(
                        new CheckResult(State.GREEN, name, value, 0, 0, check.getGroup())
                                .withOrder(0)
                                .withLink(lastBuild.getUrl())
                                .withTeams(check.getTeams())
                                .withIconSrc("https://cdn4.iconfinder.com/data/icons/keynote-and-powerpoint-icons/256/parameters-512.png"))
                );

        // add stages as check results
        checkResults.addAll(
                pipeDefinition.stream()
                        .map(stage ->
                                {
                                    int checkIndex = pipeDefinition.indexOf(stage) + 1;
                                    return checkResult(stage, lastBuildStages, check.getGroup())
                                            .withOrder(checkIndex)
                                            .withLink(lastBuild.getUrl())
                                            .withTeams(check.getTeams())
                                            .withIconSrc("http://www.kidsmathgamesonline.com/images/pictures/numbers120/number" + checkIndex + ".jpg")
                                            .withCheckResultIdentifier(lastBuild.getUrl() + "_(stage" + checkIndex + ")")
                                            .withDescription(buildInfo.getStageDescriptions().getOrDefault(stage.getName(), ""));
                                }
                        )
                        .collect(Collectors.toList()));
        return checkResults;
    }

    private boolean containsStageWithName(String name, List<PipelineStage> stages) {
        return stages.stream().anyMatch(stage -> stage.getName().equals(name));
    }

    private CheckResult checkResult(PipelineStage stageDefinition, List<PipelineStage> buildStages, Group group) {

        final Optional<PipelineStage> executedStage = buildStages.stream()
                .filter(stage -> stage.getName().equals(stageDefinition.getName()))
                .findAny();

        if (executedStage.isPresent()) {
            final PipelineStage stage = executedStage.get();
            final CheckResult checkResult = new CheckResult(state(stage), stage.getName(), info(stage), 1, failCount(stage), group);
            if (stage.getStatus() == IN_PROGRESS) {
                checkResult.markRunning();
            }
            return checkResult;
        }
        return new CheckResult(State.GREY, stageDefinition.getName(), "NOT EXECUTED", 0, 0, group);
    }

    private Integer failCount(PipelineStage stage) {
        return state(stage) == State.RED ? 1 : 0;
    }

    private String info(PipelineStage stage) {
        final String status = stage.getStatus() == null ? "UNKNOWN" : stage.getStatus().name();
        return status + "(" + stage.getDurationMillis() / 1000 + " seconds" + ")";
    }

    private State state(PipelineStage stage) {

        if (stage.getStatus() == null) {
            return State.GREY;
        }
        switch (stage.getStatus()) {
            case SUCCESS:
                return State.GREEN;
            case IN_PROGRESS:
                return State.GREY;
            case PAUSED_PENDING_INPUT:
                return State.YELLOW;
            case ABORTED:
            case FAILED:
            default:
                return State.RED;
        }
    }

    private List<PipelineStage> buildStages(JenkinsServerConfiguration serverConfig, Build build) {

        if (build == null) {
            return Collections.emptyList();
        }

        final JenkinsPipelineBuildInfo buildInfo = jenkinsClient.queryWorkflowApi(build.getUrl(), serverConfig, JenkinsPipelineBuildInfo.class);
        return buildInfo.getStages() != null ? buildInfo.getStages() : Collections.emptyList();
    }
}
