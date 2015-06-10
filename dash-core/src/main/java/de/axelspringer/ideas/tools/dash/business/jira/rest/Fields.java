package de.axelspringer.ideas.tools.dash.business.jira.rest;

import de.axelspringer.ideas.tools.dash.business.jira.Priority;

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

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Fields)) {
            return false;
        }
        final Fields other = (Fields) o;
        if (!other.canEqual((Object) this)) {
            return false;
        }
        final Object this$customfield_10144 = this.customfield_10144;
        final Object other$customfield_10144 = other.customfield_10144;
        if (this$customfield_10144 == null ? other$customfield_10144 != null : !this$customfield_10144.equals(other$customfield_10144)) {
            return false;
        }
        final Object this$assignee = this.assignee;
        final Object other$assignee = other.assignee;
        if (this$assignee == null ? other$assignee != null : !this$assignee.equals(other$assignee)) {
            return false;
        }
        final Object this$priority = this.priority;
        final Object other$priority = other.priority;
        if (this$priority == null ? other$priority != null : !this$priority.equals(other$priority)) {
            return false;
        }
        final Object this$issuetype = this.issuetype;
        final Object other$issuetype = other.issuetype;
        if (this$issuetype == null ? other$issuetype != null : !this$issuetype.equals(other$issuetype)) {
            return false;
        }
        final Object this$status = this.status;
        final Object other$status = other.status;
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $customfield_10144 = this.customfield_10144;
        result = result * PRIME + ($customfield_10144 == null ? 0 : $customfield_10144.hashCode());
        final Object $assignee = this.assignee;
        result = result * PRIME + ($assignee == null ? 0 : $assignee.hashCode());
        final Object $priority = this.priority;
        result = result * PRIME + ($priority == null ? 0 : $priority.hashCode());
        final Object $issuetype = this.issuetype;
        result = result * PRIME + ($issuetype == null ? 0 : $issuetype.hashCode());
        final Object $status = this.status;
        result = result * PRIME + ($status == null ? 0 : $status.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Fields;
    }

    public String toString() {
        return "de.axelspringer.ideas.tools.dash.business.jira.rest.Fields(customfield_10144=" + this.customfield_10144 + ", assignee=" + this.assignee + ", priority=" + this.priority + ", issuetype=" + this.issuetype + ", status=" + this.status + ")";
    }
}
