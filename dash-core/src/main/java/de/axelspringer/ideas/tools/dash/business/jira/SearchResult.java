package de.axelspringer.ideas.tools.dash.business.jira;

import de.axelspringer.ideas.tools.dash.business.jira.rest.Issue;

import java.util.List;

public class SearchResult {

    private List<Issue> issues;

    public SearchResult() {
    }

    public List<Issue> getIssues() {
        return this.issues;
    }

    public void setIssues(List<Issue> issues) {
        this.issues = issues;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SearchResult)) {
            return false;
        }
        final SearchResult other = (SearchResult) o;
        if (!other.canEqual((Object) this)) {
            return false;
        }
        final Object this$issues = this.issues;
        final Object other$issues = other.issues;
        if (this$issues == null ? other$issues != null : !this$issues.equals(other$issues)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $issues = this.issues;
        result = result * PRIME + ($issues == null ? 0 : $issues.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SearchResult;
    }

    public String toString() {
        return "de.axelspringer.ideas.tools.dash.business.jira.SearchResult(issues=" + this.issues + ")";
    }
}
