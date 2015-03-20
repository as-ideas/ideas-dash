package de.axelspringer.ideas.tools.dash.business.check;

import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.business.customization.Team;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(of = {"name", "group", "team"})
public abstract class AbstractCheck implements Check {

    private final String name;

    private final Group group;

    private final Team team;

    protected AbstractCheck(String name, Group group, Team team) {
        this.name = name;
        this.group = group;
        this.team = team;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Group getStage() {
        return group;
    }

    @Override
    public Team getTeam() {
        return team;
    }
}
