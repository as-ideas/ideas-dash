package de.axelspringer.ideas.tools.dash.example.business;

import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.check.CheckProvider;
import de.axelspringer.ideas.tools.dash.business.failure.FailingCheck;
import de.axelspringer.ideas.tools.dash.example.config.ExampleGroups;
import de.axelspringer.ideas.tools.dash.example.config.ExampleTeam;
import de.axelspringer.ideas.tools.dash.presentation.State;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;

@Component
public class ExampleCheckProvider implements CheckProvider {

    @Override
    public List<Check> provideChecks() {

        List<Check> checks = ExampleGroups.exampleGroups().stream()
                .map(group -> new ExampleCheck("Jira issues of " + group.getJiraName(), group, singletonList(ExampleTeam.BE)))
                .collect(Collectors.toList());
        // We want some empty group
        checks.remove(1);

        checks.add(new FailingCheck("test1", "message"));
        checks.add(new ExampleCheck("test2", ExampleGroups.FIRST, singletonList(ExampleTeam.FE)).withLink("link ONE"));
        checks.add(new ExampleCheck("test3", ExampleGroups.FIRST, singletonList(ExampleTeam.BE)).withLink("link TWO"));

        checks.add(new ExampleCheck("Green Check ONE", ExampleGroups.SECOND, singletonList(ExampleTeam.FE)).withState(State.GREEN));
        checks.add(new ExampleCheck("Green Check TWO", ExampleGroups.SECOND, singletonList(ExampleTeam.FE)).withState(State.GREEN));

        checks.add(new StateToggleCheck("State toggle", ExampleGroups.FIRST, singletonList(ExampleTeam.FE)));

        return checks;
    }
}
