package de.axelspringer.ideas.tools.dash.business.gocd;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.check.CheckExecutor;
import de.axelspringer.ideas.tools.dash.business.check.CheckResult;
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

                    addChecksForPipeline(restClient, check, result, pipelineName);
                }

            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return result;
    }

    private void addChecksForPipeline(CloseableHttpClientRestClient restClient, GoCdCheck check, List<CheckResult> result, String pipelineName) throws IOException {
        GoCdPipelineHistory pipelineHistory = objectMapper.readValue(restClient.get(check.config.goCdServerBaseUrl + "/api/pipelines/" + pipelineName + "/history"), GoCdPipelineHistory.class);

        if (pipelineHistory.pipelines == null || pipelineHistory.pipelines.isEmpty()) {
            result.add(new CheckResult(State.YELLOW, pipelineName, "NO PIPELINES FOUND", 0, 0, check.getGroup()));
        } else {
            // Only take the last pipeline, as it is the most current one
            GoCdPipeline latestPipeline = pipelineHistory.pipelines.get(0);

            for (GoCdStage stage : latestPipeline.stages) {
                for (GoCdJob job : stage.jobs) {
                    State resultState = isPassedOrCancelled(job) ? State.GREEN : State.RED;
                    result.add(new CheckResult(resultState, latestPipeline.name + " - " + stage.name + " - " + job.name, job.result, 0, 0, check.getGroup()));
                }

            }
        }


    }

    private boolean isPassedOrCancelled(GoCdJob job) {
        return job.result.equalsIgnoreCase("Passed") || job.result.equalsIgnoreCase("Cancelled");
    }

//    private CheckResult convertToCheckResult(PingdomAnswer pingdomAnswer, de.axelspringer.ideas.tools.dash.business.pingdom.PingdomCheck check) {
//        State state = State.RED;
//        if ("up".equals(pingdomAnswer.status) || "paused".equals(pingdomAnswer.status)) {
//            state = State.GREEN;
//        }
//
//        return new CheckResult(state, pingdomAnswer.status + ": " + pingdomAnswer.name, pingdomAnswer.hostname, 1, 1, check.getGroup())
//                .withLink("https://my.pingdom.com/newchecks/checks#check=" + pingdomAnswer.id);
//    }
//
//    private boolean isNameMatching(PingdomAnswer candidate, de.axelspringer.ideas.tools.dash.business.pingdom.PingdomCheck check) {
//        Matcher matcher = check.pattern().matcher(candidate.name);
//        return matcher.matches();
//    }


    @Override
    public boolean isApplicable(Check check) {
        return check instanceof GoCdCheck;
    }
}
