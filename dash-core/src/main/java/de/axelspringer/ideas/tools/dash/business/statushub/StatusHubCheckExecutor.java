package de.axelspringer.ideas.tools.dash.business.statushub;

import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.check.CheckExecutor;
import de.axelspringer.ideas.tools.dash.business.check.CheckResult;
import de.axelspringer.ideas.tools.dash.presentation.State;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class StatusHubCheckExecutor implements CheckExecutor<StatusHubCheck> {

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public List<CheckResult> executeCheck(StatusHubCheck check) {

        final String link = "http://" + check.getId() + ".statushub.io/";
        final String markup = restTemplate.getForObject(link, String.class);

        final Document document = Jsoup.parse(markup);
        final int incidentCount = document.select(".incident").size();

        return Collections.singletonList(
                new CheckResult(incidentCount > 0 ? State.RED : State.GREEN, "Statushub", incidentCount + " incidents on statushub", 1, incidentCount, check.getGroup())
                        .withLink(link)
                        .withTeams(check.getTeams())
        );
    }

    @Override
    public boolean isApplicable(Check check) {
        return check instanceof StatusHubCheck;
    }
}
