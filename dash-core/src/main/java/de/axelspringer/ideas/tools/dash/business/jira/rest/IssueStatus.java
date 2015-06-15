package de.axelspringer.ideas.tools.dash.business.jira.rest;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

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
