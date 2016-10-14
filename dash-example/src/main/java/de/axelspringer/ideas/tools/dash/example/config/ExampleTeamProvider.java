package de.axelspringer.ideas.tools.dash.example.config;

import de.axelspringer.ideas.tools.dash.business.customization.Team;
import de.axelspringer.ideas.tools.dash.business.customization.TeamProvider;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ExampleTeamProvider implements TeamProvider {

    @Override
    public List<Team> getTeams() {
        return Arrays.asList(ExampleTeam.values());
    }
}
