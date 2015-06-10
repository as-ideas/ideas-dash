package de.axelspringer.ideas.tools.dash.presentation;

import java.util.List;

public class UiTeams {

    private final List<String> teams;

    @java.beans.ConstructorProperties({"teams"})
    public UiTeams(List<String> teams) {
        this.teams = teams;
    }

    public List<String> getTeams() {
        return this.teams;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UiTeams)) {
            return false;
        }
        final UiTeams other = (UiTeams) o;
        if (!other.canEqual((Object) this)) {
            return false;
        }
        final Object this$teams = this.teams;
        final Object other$teams = other.teams;
        if (this$teams == null ? other$teams != null : !this$teams.equals(other$teams)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $teams = this.teams;
        result = result * PRIME + ($teams == null ? 0 : $teams.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof UiTeams;
    }

    public String toString() {
        return "de.axelspringer.ideas.tools.dash.presentation.UiTeams(teams=" + this.teams + ")";
    }
}
