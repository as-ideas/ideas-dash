package de.axelspringer.ideas.tools.dash.business.statushub;

import de.axelspringer.ideas.tools.dash.business.check.AbstractCheck;
import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.business.customization.Team;

public class StatusHubCheck extends AbstractCheck {

    private static final String ICON_SRC = "assets/status-hub-logo.png";
    private final String id;

    public StatusHubCheck(String name, Group group, Team team, String id) {
        super(name, group, team);
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
