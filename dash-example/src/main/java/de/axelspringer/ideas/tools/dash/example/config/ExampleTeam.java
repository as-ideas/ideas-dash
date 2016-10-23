package de.axelspringer.ideas.tools.dash.example.config;

import de.axelspringer.ideas.tools.dash.business.customization.Team;

public enum ExampleTeam implements Team {

    FE("'Frontend'"), BE("'Backend'");

    private final String jiraTeamName;

    ExampleTeam(String jiraTeamName) {
        this.jiraTeamName = jiraTeamName;
    }

    @Override
    public String getTeamName() {
        return name();
    }

    @Override
    public String getJiraTeamName() {
        return jiraTeamName;
    }
}