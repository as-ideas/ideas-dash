package de.axelspringer.ideas.tools.dash.business.stash;

import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.check.CheckExecutor;
import de.axelspringer.ideas.tools.dash.business.check.checkresult.CheckResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StashCheckExecutor implements CheckExecutor<StashCheck> {

    @Autowired
    private StashApiClient stashApiClient;

    @Autowired
    private StateIndicator stateIndicator;

    @Autowired
    private StashConfig stashConfig;

    @Override
    public List<CheckResult> executeCheck(StashCheck check) {

        if (!stashConfig.isEnabled()) {
            return Collections.emptyList();
        }

        return stashApiClient.fetchStashRepos().stream().map(repo -> stashApiClient.fetchPullRequestsOfRepo(repo)).flatMap(Collection::stream).map(pullRequest -> {
            final StateIndicator.IndicateResult indicateResult = stateIndicator.check(pullRequest);
            return new CheckResult(indicateResult.getState(), "Merge Request " + pullRequest.repo().getName(), indicateResult.getInfo(), 1, 1, check.getGroup())
                    .withTeams(check.getTeams())
                    .withLink(stashConfig.pullRequestAccessUrl(pullRequest.repo().getName(), pullRequest.id()));

        }).collect(Collectors.toList());
    }

    @Override
    public boolean isApplicable(Check check) {
        return check instanceof StashCheck;
    }


}
