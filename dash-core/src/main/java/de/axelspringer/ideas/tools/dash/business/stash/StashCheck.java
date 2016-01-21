package de.axelspringer.ideas.tools.dash.business.stash;

import java.util.List;
import de.axelspringer.ideas.tools.dash.business.check.AbstractCheck;
import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.business.customization.Team;

public class StashCheck extends AbstractCheck {

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
