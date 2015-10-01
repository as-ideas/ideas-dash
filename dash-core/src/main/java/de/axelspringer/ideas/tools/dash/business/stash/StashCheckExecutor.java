package de.axelspringer.ideas.tools.dash.business.stash;

import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.check.CheckExecutor;
import de.axelspringer.ideas.tools.dash.business.check.CheckResult;
import de.axelspringer.ideas.tools.dash.presentation.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Service
public class StashCheckExecutor implements CheckExecutor<StashCheck> {

    @Autowired
    private StashApiClient stashApiClient;

    @Override
    public List<CheckResult> executeCheck(StashCheck check) {
        List<CheckResult> checkResults = new ArrayList<>();

        for (StashRepo stashRepo : stashApiClient.readStashRepos(check.stashConfig())) {
            for (StashPullRequest stashPullRequest : stashApiClient.readStashPullRequests(check.stashConfig(), stashRepo)) {
                final List<StashUser> reviewers = stashPullRequest.reviewers();
                reviewerNamesAsString(reviewers);

                State state = reviewers.size() == 0 ? State.RED : State.YELLOW;
                final CheckResult checkResult = new CheckResult(state, "Merge Request " + stashRepo.name(), "[" + reviewerNamesAsString(reviewers) + "]", 1, 1, check.getGroup());
                checkResult.withLink(check.stashConfig().stashServerUrl() + "/projects/PCP/repos/" + stashPullRequest.repo().name() + "/pull-requests/" + stashPullRequest.id());
                checkResult.withTeam(check.getTeam());
                checkResults.add(checkResult);
            }
        }

        return checkResults;
    }

    private String reviewerNamesAsString(List<StashUser> reviewers) {
        StringJoiner reviewerNames = new StringJoiner(",");
        for (StashUser reviewer : reviewers) {
            reviewerNames.add(reviewer.name());
        }
        return reviewerNames.toString();
    }

    @Override
    public boolean isApplicable(Check check) {
        return check instanceof StashCheck;
    }


}
