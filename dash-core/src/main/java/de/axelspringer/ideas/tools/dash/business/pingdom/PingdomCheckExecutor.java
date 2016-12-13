package de.axelspringer.ideas.tools.dash.business.pingdom;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.check.CheckExecutor;
import de.axelspringer.ideas.tools.dash.business.check.checkresult.CheckResult;
import de.axelspringer.ideas.tools.dash.presentation.State;
import de.axelspringer.ideas.tools.dash.util.CloseableHttpClientRestClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

@Service
public class PingdomCheckExecutor implements CheckExecutor<PingdomCheck> {

    @Autowired
    private CloseableHttpClient closeableHttpClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public List<CheckResult> executeCheck(PingdomCheck check) {
        try {

            PingdomConfig config = check.pingdomConfig();


            final CloseableHttpClientRestClient restClient = new CloseableHttpClientRestClient(closeableHttpClient)
                    .withCredentials(config.userName, config.password)
                    .withHeader("App-Key", config.appKey)
                    .withHeader("Account-Email", config.accountEmail);

            String answer = restClient.get("https://api.pingdom.com/api/2.0/checks");


            PingdomAnswerWrapper pingdomAnswerWrapper = objectMapper.readValue(answer, PingdomAnswerWrapper.class);

            return pingdomAnswerWrapper.checks.stream()
                    .filter(candidate -> isNameMatching(candidate, check))
                    .map(pingdomAnswer -> convertToCheckResult(pingdomAnswer, check))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private CheckResult convertToCheckResult(PingdomAnswer pingdomAnswer, PingdomCheck check) {
        State state = State.RED;
        if ("up".equals(pingdomAnswer.status) || "paused".equals(pingdomAnswer.status)) {
            state = State.GREEN;
        }

        return new CheckResult(state, pingdomAnswer.status + ": " + pingdomAnswer.name, pingdomAnswer.hostname, 1, 1, check.getGroup())
                .withLink("https://my.pingdom.com/newchecks/checks#check=" + pingdomAnswer.id)
                .withTeams(check.getTeams());
    }

    private boolean isNameMatching(PingdomAnswer candidate, PingdomCheck check) {
        Matcher matcher = check.pattern().matcher(candidate.name);
        return matcher.matches();
    }


    @Override
    public boolean isApplicable(Check check) {
        return check instanceof PingdomCheck;
    }
}
