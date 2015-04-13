package de.axelspringer.ideas.tools.dash.business.check;

import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.business.customization.Team;

/**
 * Defines the model check belonging to a team and group.
 */
public interface Check {

    public String getName();

    public Group getGroup();

    public Team getTeam();
}
