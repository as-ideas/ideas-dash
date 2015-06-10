package de.axelspringer.ideas.tools.dash.business.jira.rest;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Issue {

    private String key;

    private Fields fields;

    public Issue() {
    }

    public boolean isBug() {
        return fields.isBug();
    }

    public IssueType getIssueType() {
        return getFields().getIssuetype();
    }

    public IssueStatus getIssueStatus() {
        return getFields().getStatus();
    }

    public String getKey() {
        return this.key;
    }

    public Fields getFields() {
        return this.fields;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setFields(Fields fields) {
        this.fields = fields;
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
