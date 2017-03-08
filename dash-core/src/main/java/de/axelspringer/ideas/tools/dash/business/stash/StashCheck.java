package de.axelspringer.ideas.tools.dash.business.stash;

import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.business.customization.Team;

import java.util.List;

public class StashCheck extends Check {

    private static final String IMG_SRC = "assets/stash-logo.png";

    public StashCheck(String name, Group group, List<Team> teams) {
        super(name, group, teams);
    }

    @Override
    public String getIconSrc() {
        return IMG_SRC;
    }
}
