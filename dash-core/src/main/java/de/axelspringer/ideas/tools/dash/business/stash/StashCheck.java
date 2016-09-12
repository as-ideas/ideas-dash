package de.axelspringer.ideas.tools.dash.business.stash;

import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.business.customization.Team;

import java.util.List;

public class StashCheck extends Check {

    private static final String IMG_SRC = "assets/stash-logo.png";

    private final StashConfig stashConfig;

    public StashCheck(String name, Group group, List<Team> teams, StashConfig stashConfig) {
        super(name, group, teams);
        this.stashConfig = stashConfig;
    }

    public StashConfig stashConfig() {
        return stashConfig;
    }

    @Override
    public String getIconSrc() {
        return IMG_SRC;
    }
}
