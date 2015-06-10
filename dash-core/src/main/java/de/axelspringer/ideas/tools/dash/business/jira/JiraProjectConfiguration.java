package de.axelspringer.ideas.tools.dash.business.jira;

import de.axelspringer.ideas.tools.dash.business.jira.rest.Issue;
import de.axelspringer.ideas.tools.dash.presentation.State;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JiraProjectConfiguration {

    private final List<String> issueStatesInProgress = new ArrayList<>();

    private final Map<String, State> issueStateToStateMapping = new HashMap<>();

    public JiraProjectConfiguration() {
    }

    public JiraProjectConfiguration addIssueStateInProgress(String state) {

        issueStatesInProgress.add(state);
        return this;
    }

    /**
     * maps the given issue state to a specific state
     *
     * @param issueState  will be mapped
     * @param mappedState against this state
     * @return this {@link JiraProjectConfiguration} instance
     */
    public JiraProjectConfiguration withIssueStateToStateMapping(String issueState, State mappedState) {

        issueStateToStateMapping.put(issueState, mappedState);
        return this;
    }

    /**
     * @param issueState will be resolved in {@link #issueStateToStateMapping}
     * @return corresponding state
     */
    public State stateForIssueState(String issueState) {

        return issueStateToStateMapping.get(issueState);
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

    public List<String> getIssueStatesInProgress() {
        return this.issueStatesInProgress;
    }

    public Map<String, State> getIssueStateToStateMapping() {
        return this.issueStateToStateMapping;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof JiraProjectConfiguration)) {
            return false;
        }
        final JiraProjectConfiguration other = (JiraProjectConfiguration) o;
        if (!other.canEqual((Object) this)) {
            return false;
        }
        final Object this$issueStatesInProgress = this.issueStatesInProgress;
        final Object other$issueStatesInProgress = other.issueStatesInProgress;
        if (this$issueStatesInProgress == null ? other$issueStatesInProgress != null : !this$issueStatesInProgress.equals(other$issueStatesInProgress)) {
            return false;
        }
        final Object this$issueStateToStateMapping = this.issueStateToStateMapping;
        final Object other$issueStateToStateMapping = other.issueStateToStateMapping;
        if (this$issueStateToStateMapping == null ? other$issueStateToStateMapping != null : !this$issueStateToStateMapping.equals(other$issueStateToStateMapping)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $issueStatesInProgress = this.issueStatesInProgress;
        result = result * PRIME + ($issueStatesInProgress == null ? 0 : $issueStatesInProgress.hashCode());
        final Object $issueStateToStateMapping = this.issueStateToStateMapping;
        result = result * PRIME + ($issueStateToStateMapping == null ? 0 : $issueStateToStateMapping.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof JiraProjectConfiguration;
    }

    public String toString() {
        return "de.axelspringer.ideas.tools.dash.business.jira.JiraProjectConfiguration(issueStatesInProgress=" + this.issueStatesInProgress + ", issueStateToStateMapping=" + this.issueStateToStateMapping + ")";
    }
}
