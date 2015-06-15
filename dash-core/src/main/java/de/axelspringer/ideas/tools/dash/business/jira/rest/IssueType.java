package de.axelspringer.ideas.tools.dash.business.jira.rest;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

// eg. "issuetype":{"self": "https://as-jira.axelspringer.de/rest/api/2/issuetype/37", "id": "37", "description": "Eine User Story", "iconUrl": "https://as-jira.axelspringer.de/images/icons/issuetypes/story.png", "name": "Story", "subtask": false},
public class IssueType {
    private String self;
    private String id;
    private String description;
    private String name;

    public IssueType() {
    }

    public String getSelf() {
        return this.self;
    }

    public String getId() {
        return this.id;
    }

    public String getDescription() {
        return this.description;
    }

    public String getName() {
        return this.name;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
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
