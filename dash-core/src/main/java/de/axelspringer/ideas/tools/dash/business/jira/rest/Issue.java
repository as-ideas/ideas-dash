package de.axelspringer.ideas.tools.dash.business.jira.rest;

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

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Issue)) {
            return false;
        }
        final Issue other = (Issue) o;
        if (!other.canEqual((Object) this)) {
            return false;
        }
        final Object this$key = this.key;
        final Object other$key = other.key;
        if (this$key == null ? other$key != null : !this$key.equals(other$key)) {
            return false;
        }
        final Object this$fields = this.fields;
        final Object other$fields = other.fields;
        if (this$fields == null ? other$fields != null : !this$fields.equals(other$fields)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $key = this.key;
        result = result * PRIME + ($key == null ? 0 : $key.hashCode());
        final Object $fields = this.fields;
        result = result * PRIME + ($fields == null ? 0 : $fields.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Issue;
    }

    public String toString() {
        return "de.axelspringer.ideas.tools.dash.business.jira.rest.Issue(key=" + this.key + ", fields=" + this.fields + ")";
    }
}
