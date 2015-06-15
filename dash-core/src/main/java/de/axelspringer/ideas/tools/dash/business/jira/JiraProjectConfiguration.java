package de.axelspringer.ideas.tools.dash.business.jira;

import de.axelspringer.ideas.tools.dash.business.jira.rest.Issue;
import de.axelspringer.ideas.tools.dash.presentation.State;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

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

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
