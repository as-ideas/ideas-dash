package de.axelspringer.ideas.tools.dash.business.check;

import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.business.customization.Team;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCheck implements Check {

    private final String name;

    private final Group group;

    private final List<Team> teams;

    protected AbstractCheck(String name, Group group, List<Team> teams) {
        this.name = name;
        this.group = group;
        this.teams = teams != null ? teams : new ArrayList<>();
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
    public List<Team> getTeams() {
        return teams;
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
