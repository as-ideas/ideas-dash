package de.axelspringer.ideas.tools.dash.example.business;

import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.business.customization.Team;

import java.util.List;

public class StateToggleCheck extends Check {

    public StateToggleCheck(String name, Group group, List<Team> teams) {
        super(name, group, teams);
    }
}
