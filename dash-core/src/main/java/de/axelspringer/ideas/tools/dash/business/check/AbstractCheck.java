package de.axelspringer.ideas.tools.dash.business.check;

import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.business.customization.Team;

public abstract class AbstractCheck implements Check {

    private final String name;

    private final Group group;

    private final Team team;

    protected AbstractCheck(String name, Group group, Team team) {
        this.name = name;
        this.group = group;
        this.team = team;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Group getGroup() {
        return group;
    }

    @Override
    public Team getTeam() {
        return team;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AbstractCheck)) {
            return false;
        }
        final AbstractCheck other = (AbstractCheck) o;
        if (!other.canEqual((Object) this)) {
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        final Object this$group = this.getGroup();
        final Object other$group = other.getGroup();
        if (this$group == null ? other$group != null : !this$group.equals(other$group)) {
            return false;
        }
        final Object this$team = this.getTeam();
        final Object other$team = other.getTeam();
        if (this$team == null ? other$team != null : !this$team.equals(other$team)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 0 : $name.hashCode());
        final Object $group = this.getGroup();
        result = result * PRIME + ($group == null ? 0 : $group.hashCode());
        final Object $team = this.getTeam();
        result = result * PRIME + ($team == null ? 0 : $team.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof AbstractCheck;
    }
}
