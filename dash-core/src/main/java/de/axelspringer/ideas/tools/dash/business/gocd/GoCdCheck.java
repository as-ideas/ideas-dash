package de.axelspringer.ideas.tools.dash.business.gocd;

import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.business.customization.Team;

import java.util.Collections;
import java.util.List;

public class GoCdCheck extends Check {

    public final GoCdConfig config;

    public GoCdCheck(String name, Group group, Team team, GoCdConfig config) {
        this(name, group, Collections.singletonList(team), config);
    }

    public GoCdCheck(String name, Group group, List<Team> teams, GoCdConfig config) {
        super(name, group, teams);
        this.config = config;
    }

    @Override
    public String getIconSrc() {
        return "assets/gocd-logo.png";
    }
}
