package de.axelspringer.ideas.tools.dash.business.stash;

import de.axelspringer.ideas.tools.dash.presentation.State;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.StringJoiner;

@Component
public class DefaultStashPullRequestStateIndicator implements StateIndicator<StashPullRequest> {

    @Override
    public IndicateResult check(StashPullRequest stashPullRequest) {
        final List<StashUser> reviewers = stashPullRequest.reviewers();
        reviewerNamesAsString(reviewers);

        State state = reviewers.size() == 0 ? State.RED : State.YELLOW;
        String checkResultInfo = stashPullRequest.getAgeInDays() + "d " + "[" + reviewerNamesAsString(reviewers) + "]";

        return new IndicateResult(state, checkResultInfo);
    }

    private String reviewerNamesAsString(List<StashUser> reviewers) {
        StringJoiner reviewerNames = new StringJoiner(",");
        for (StashUser reviewer : reviewers) {
            reviewerNames.add(reviewer.name());
        }
        return reviewerNames.toString();
    }
}
