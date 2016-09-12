package de.axelspringer.ideas.tools.dash.business.statushub;

import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.business.customization.Team;

import java.util.List;

public class StatusHubCheck extends Check {

    private static final String ICON_SRC = "assets/status-hub-logo.png";
    private final String id;

    public StatusHubCheck(String name, Group group, List<Team> teams, String id) {
        super(name, group, teams);
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    @Override
    public String getIconSrc() {
        return ICON_SRC;
    }
}
