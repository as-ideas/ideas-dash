package de.axelspringer.ideas.tools.dash.business.check;

import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.business.customization.Team;

/**
 * Defines the model check belonging to a team and group.
 */
public interface Check {

    String getName();

    Group getGroup();

    Team getTeam();

    /**
     * Implement this method to provide an image source path for the check to be visibale in the monitor
     * @return the [relative] image source path
     */
    default String getIconSrc(){
        return null;
    }

}
