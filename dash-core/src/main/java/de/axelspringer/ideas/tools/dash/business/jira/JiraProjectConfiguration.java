package de.axelspringer.ideas.tools.dash.business.jira;

import de.axelspringer.ideas.tools.dash.business.jira.rest.Issue;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class JiraProjectConfiguration {

    private final List<String> issueStatesInProgress = new ArrayList<>();

    public void addIssueStateInProgress(String state) {
        issueStatesInProgress.add(state);
    }

    public boolean isIssueInProgress(Issue issue) {
        String issueState = issue.getIssueStatus().getName();
        for (String issueStateInProgress : issueStatesInProgress) {
            if (issueStateInProgress.equalsIgnoreCase(issueState)) {
                return true;
            }
        }
        return false;
    }
}
