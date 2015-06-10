package de.axelspringer.ideas.tools.dash.business.jira.rest;

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

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof IssueType)) {
            return false;
        }
        final IssueType other = (IssueType) o;
        if (!other.canEqual((Object) this)) {
            return false;
        }
        final Object this$self = this.self;
        final Object other$self = other.self;
        if (this$self == null ? other$self != null : !this$self.equals(other$self)) {
            return false;
        }
        final Object this$id = this.id;
        final Object other$id = other.id;
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) {
            return false;
        }
        final Object this$description = this.description;
        final Object other$description = other.description;
        if (this$description == null ? other$description != null : !this$description.equals(other$description)) {
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
        final Object $self = this.self;
        result = result * PRIME + ($self == null ? 0 : $self.hashCode());
        final Object $id = this.id;
        result = result * PRIME + ($id == null ? 0 : $id.hashCode());
        final Object $description = this.description;
        result = result * PRIME + ($description == null ? 0 : $description.hashCode());
        final Object $name = this.name;
        result = result * PRIME + ($name == null ? 0 : $name.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof IssueType;
    }

    public String toString() {
        return "de.axelspringer.ideas.tools.dash.business.jira.rest.IssueType(self=" + this.self + ", id=" + this.id + ", description=" + this.description + ", name=" + this.name + ")";
    }
}
