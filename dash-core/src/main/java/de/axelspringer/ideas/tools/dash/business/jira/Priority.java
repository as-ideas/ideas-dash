package de.axelspringer.ideas.tools.dash.business.jira;

public class Priority {

    public final static String BLOCKER_NAME = "Blocker";

    private String name;

    public Priority() {
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
        if (!(o instanceof Priority)) {
            return false;
        }
        final Priority other = (Priority) o;
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
        return other instanceof Priority;
    }

    public String toString() {
        return "de.axelspringer.ideas.tools.dash.business.jira.Priority(name=" + this.name + ")";
    }
}
