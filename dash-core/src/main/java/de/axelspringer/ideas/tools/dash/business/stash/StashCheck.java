package de.axelspringer.ideas.tools.dash.business.stash;

import de.axelspringer.ideas.tools.dash.business.check.AbstractCheck;
import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.business.customization.Team;

public class StashCheck extends AbstractCheck {

    private final StashConfig stashConfig;

    public StashCheck(String name, Group group, Team team, StashConfig stashConfig) {
        super(name, group, team);
        this.stashConfig = stashConfig;
    }

    public StashConfig stashConfig() {
        return stashConfig;
    }
}
