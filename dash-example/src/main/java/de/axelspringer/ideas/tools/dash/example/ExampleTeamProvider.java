package de.axelspringer.ideas.tools.dash.example;

import de.axelspringer.ideas.tools.dash.business.customization.Team;
import de.axelspringer.ideas.tools.dash.business.customization.TeamProvider;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class ExampleTeamProvider implements TeamProvider {

    @Override
    public List<Team> getTeams() {
        return Collections.singletonList(new Team() {
            @Override
            public String getTeamName() {
                return "example";
            }

            @Override
            public String getJiraTeamName() {
                return "n/a";
            }
        });
    }
}
