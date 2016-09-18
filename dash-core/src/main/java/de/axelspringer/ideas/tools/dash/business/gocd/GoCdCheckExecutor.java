package de.axelspringer.ideas.tools.dash.business.gocd;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.check.CheckExecutor;
import de.axelspringer.ideas.tools.dash.business.check.checkresult.CheckResult;
import de.axelspringer.ideas.tools.dash.presentation.State;
import de.axelspringer.ideas.tools.dash.util.CloseableHttpClientRestClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class GoCdCheckExecutor implements CheckExecutor<GoCdCheck> {

    @Autowired
    private CloseableHttpClient closeableHttpClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public List<CheckResult> executeCheck(GoCdCheck check) {
        List<CheckResult> result = new ArrayList<>();

        try {
            // Query all pipeline groups
            // http://ideas-ci/go/api/config/pipeline_groups
            final CloseableHttpClientRestClient restClient = new CloseableHttpClientRestClient(closeableHttpClient);

            String pipelineGroupResult = restClient.get(check.config.goCdServerBaseUrl + "/api/config/pipeline_groups");
            JsonNode pipelineGroupResultNode = objectMapper.readTree(pipelineGroupResult);
            for (JsonNode pipelineGroups : pipelineGroupResultNode) {
                String pipelineGroupName = pipelineGroups.get("name").asText();

                for (JsonNode pipeline : pipelineGroups.get("pipelines")) {
                    String pipelineName = pipeline.get("name").asText();

                    result.add(addChecksForPipeline(restClient, check, pipelineName));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return result;
    }

    private CheckResult addChecksForPipeline(CloseableHttpClientRestClient restClient, GoCdCheck check, String pipelineName) throws IOException {
        GoCdPipelineHistory pipelineHistory = objectMapper.readValue(restClient.get(check.config.goCdServerBaseUrl + "/api/pipelines/" + pipelineName + "/history"), GoCdPipelineHistory.class);

        if (pipelineHistory.pipelines == null || pipelineHistory.pipelines.isEmpty()) {
            return new CheckResult(State.GREEN, pipelineName, "PIPELINE HAS NO RUNS", 0, 0, check.getGroup());
        } else {
            // Only take the last pipeline, as it is the most current one
            GoCdPipeline latestPipeline = pipelineHistory.pipelines.get(0);
            return determineResultDetails(restClient, check, latestPipeline);
        }
    }

    private CheckResult determineResultDetails(CloseableHttpClientRestClient restClient, GoCdCheck check, GoCdPipeline pipeline) throws IOException {
        String resInfo = "Passed";
        String stageAndJobAffected = "";
        State resultState = State.RED;
        boolean buildRunning = false;

        for (GoCdStage stage : pipeline.stages) {
            if (isPassedOrCancelled(stage)) {
                resultState = State.GREEN;
            } else {
                resultState = State.RED;
            }

            for (GoCdJob job : stage.jobs) {
                if (isJobRunning(job)) {
                    stageAndJobAffected = stage.name + " - " + job.name;
                    resInfo = "In Progress";
                    buildRunning = true;
                    resultState = State.YELLOW;
                } else if (!isPassedOrCancelled(job)) {
                    stageAndJobAffected = stage.name + " - " + job.name;
                    resInfo = job.result;
                }
            }

            if (buildRunning || resultState == State.RED) {
                break;
            }
        }

        if (!pipeline.can_run && State.RED == resultState) {
            // It might be deactivated and thus should be green
            boolean paused = objectMapper.readTree(restClient.get(
                    check.config.goCdServerBaseUrl + "/api/pipelines/" + pipeline.name + "/status"))
                    .get("paused").asBoolean();
            if (paused) {
                resultState = State.GREEN;
                resInfo = "PAUSED";
            }
        }

        final CheckResult res = new CheckResult(resultState, pipeline.name + " - " + stageAndJobAffected, resInfo, 0, 0, check.getGroup());
        if(buildRunning){
            res.markRunning();
        }
        return res;
    }

    private boolean isPassedOrCancelled(GoCdStage stage) {
        return stage.result == null || "Passed".equalsIgnoreCase(stage.result) || "Cancelled".equalsIgnoreCase(stage.result);
    }

    private boolean isJobRunning(GoCdJob job) {
        return "Building".equalsIgnoreCase(job.state);
    }

    private boolean isPassedOrCancelled(GoCdJob job) {
        return "Passed".equalsIgnoreCase(job.result) || "Cancelled".equalsIgnoreCase(job.result);
    }

    @Override
    public boolean isApplicable(Check check) {
        return check instanceof GoCdCheck;
    }
}
