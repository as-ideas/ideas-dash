package de.axelspringer.ideas.tools.dash.business.check;

import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.business.customization.Team;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines the model check belonging to a team and group.
 */
public class Check {

    private final String name;
    private final Group group;
    private final List<Team> teams;

    private String iconSrc;

    public Check(String name, Group group, List<Team> teams) {
        this.name = name;
        this.group = group;
        this.teams = teams != null ? teams : new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public Group getGroup() {
        return group;
    }

    public List<Team> getTeams() {
        return teams;
    }

    /**
     * Implement this method to provide an image source path for the check to be visibale in the monitor
     *
     * @return the [relative] image source path
     */
    public String getIconSrc() {
        return iconSrc;
    }

    public void setIconSrc(String iconSrc) {
        this.iconSrc = iconSrc;
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
