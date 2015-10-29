package de.axelspringer.ideas.tools.dash.business.jira;

import de.axelspringer.ideas.tools.dash.business.jira.rest.Issue;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class JiraProjectConfiguration {

    private final List<String> issueStatesInProgress = new ArrayList<>();

    public JiraProjectConfiguration() {
    }

    public JiraProjectConfiguration addIssueStateInProgress(String state) {

        issueStatesInProgress.add(state);
        return this;
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
