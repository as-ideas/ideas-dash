package de.axelspringer.ideas.tools.dash.business.jira.rest;

import de.axelspringer.ideas.tools.dash.business.jira.Priority;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Fields {

    /**
     * Team
     */
    private CustomField customfield_10144;

    private Assignee assignee;

    private Priority priority;

    private IssueType issuetype;

    private IssueStatus status;

    public Fields() {
    }

    public boolean isBug() {
        return issuetype.getName().equalsIgnoreCase("Bug");
    }

    public CustomField getCustomfield_10144() {
        return this.customfield_10144;
    }

    public Assignee getAssignee() {
        return this.assignee;
    }

    public Priority getPriority() {
        return this.priority;
    }

    public IssueType getIssuetype() {
        return this.issuetype;
    }

    public IssueStatus getStatus() {
        return this.status;
    }

    public void setCustomfield_10144(CustomField customfield_10144) {
        this.customfield_10144 = customfield_10144;
    }

    public void setAssignee(Assignee assignee) {
        this.assignee = assignee;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public void setIssuetype(IssueType issuetype) {
        this.issuetype = issuetype;
    }

    public void setStatus(IssueStatus status) {
        this.status = status;
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
