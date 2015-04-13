package de.axelspringer.ideas.tools.dash.example;

import de.axelspringer.ideas.tools.dash.business.check.AbstractCheck;
import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.business.customization.Team;

public class ExampleCheck extends AbstractCheck {

    protected ExampleCheck(String name, Group group, Team team) {
        super(name, group, team);
    }
}
