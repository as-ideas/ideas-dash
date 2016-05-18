package de.axelspringer.ideas.tools.dash.business.gocd;

import de.axelspringer.ideas.tools.dash.business.check.AbstractCheck;
import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.business.customization.Team;

public class GoCdCheck extends AbstractCheck {

    public final GoCdConfig config;

    public GoCdCheck(String name, Group group, Team team, GoCdConfig config) {
        super(name, group, null);
        this.config = config;
    }

    @Override
    public String getIconSrc() {
        return "assets/gocd-logo.png";
    }
}
