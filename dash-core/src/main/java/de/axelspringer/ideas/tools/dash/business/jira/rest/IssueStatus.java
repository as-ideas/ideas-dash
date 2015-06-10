package de.axelspringer.ideas.tools.dash.business.jira.rest;

/**
 * status": {
 * "self": "https://as-jira.axelspringer.de/rest/api/2/status/1",
 * "description": "The issue is open and ready for the assignee to start work on it.",
 * "iconUrl": "https://as-jira.axelspringer.de/images/icons/statuses/open.png",
 * "name": "Open",
 * "id": "1",
 * "statusCategory": {
 * "self": "https://as-jira.axelspringer.de/rest/api/2/statuscategory/2",
 * "id": 2,
 * "key": "new",
 * "colorName": "blue-gray",
 * "name": "New"
 * }
 * },
 */
public class IssueStatus {
    private String name;

    public IssueStatus() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof IssueStatus)) {
            return false;
        }
        final IssueStatus other = (IssueStatus) o;
        if (!other.canEqual((Object) this)) {
            return false;
        }
        final Object this$name = this.name;
        final Object other$name = other.name;
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.name;
        result = result * PRIME + ($name == null ? 0 : $name.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof IssueStatus;
    }

    public String toString() {
        return "de.axelspringer.ideas.tools.dash.business.jira.rest.IssueStatus(name=" + this.name + ")";
    }
}
