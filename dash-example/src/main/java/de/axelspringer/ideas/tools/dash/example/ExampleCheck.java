package de.axelspringer.ideas.tools.dash.example;

import de.axelspringer.ideas.tools.dash.business.check.AbstractCheck;
import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.business.customization.Team;
import de.axelspringer.ideas.tools.dash.presentation.State;

public class ExampleCheck extends AbstractCheck {

    private String link;

    private State state = State.YELLOW;

    protected ExampleCheck(String name, Group group, Team team) {
        super(name, group, team);
    }

    public ExampleCheck withLink(String link) {
        this.link = link;
        return this;
    }

    public String link() {
        return link;
    }

    public ExampleCheck withState(State state) {
        this.state = state;
        return this;
    }

    public State state() {
        return state;
    }
}
