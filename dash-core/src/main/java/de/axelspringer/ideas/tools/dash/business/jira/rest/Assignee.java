package de.axelspringer.ideas.tools.dash.business.jira.rest;

public class Assignee {
    private String self;
    private String name;
    private String emailAddress;
    private String displayName;

    public Assignee() {
    }

    public String getSelf() {
        return this.self;
    }

    public String getName() {
        return this.name;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Assignee)) {
            return false;
        }
        final Assignee other = (Assignee) o;
        if (!other.canEqual((Object) this)) {
            return false;
        }
        final Object this$self = this.self;
        final Object other$self = other.self;
        if (this$self == null ? other$self != null : !this$self.equals(other$self)) {
            return false;
        }
        final Object this$name = this.name;
        final Object other$name = other.name;
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        final Object this$emailAddress = this.emailAddress;
        final Object other$emailAddress = other.emailAddress;
        if (this$emailAddress == null ? other$emailAddress != null : !this$emailAddress.equals(other$emailAddress)) {
            return false;
        }
        final Object this$displayName = this.displayName;
        final Object other$displayName = other.displayName;
        if (this$displayName == null ? other$displayName != null : !this$displayName.equals(other$displayName)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $self = this.self;
        result = result * PRIME + ($self == null ? 0 : $self.hashCode());
        final Object $name = this.name;
        result = result * PRIME + ($name == null ? 0 : $name.hashCode());
        final Object $emailAddress = this.emailAddress;
        result = result * PRIME + ($emailAddress == null ? 0 : $emailAddress.hashCode());
        final Object $displayName = this.displayName;
        result = result * PRIME + ($displayName == null ? 0 : $displayName.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Assignee;
    }

    public String toString() {
        return "de.axelspringer.ideas.tools.dash.business.jira.rest.Assignee(self=" + this.self + ", name=" + this.name + ", emailAddress=" + this.emailAddress + ", displayName=" + this.displayName + ")";
    }
}
