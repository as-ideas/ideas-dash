package de.axelspringer.ideas.tools.dash;

import de.axelspringer.ideas.tools.dash.business.customization.Team;

public class TestTeam implements Team {

    public final static TestTeam INSTANCE = new TestTeam();

    @Override
    public String getTeamName() {
        return "TestTeam";
    }

    @Override
    public String getJiraTeamName() {
        return "test";
    }
}
