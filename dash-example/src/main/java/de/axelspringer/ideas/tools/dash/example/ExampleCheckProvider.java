package de.axelspringer.ideas.tools.dash.example;

import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.check.CheckProvider;
import de.axelspringer.ideas.tools.dash.business.failure.FailingCheck;
import de.axelspringer.ideas.tools.dash.presentation.State;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ExampleCheckProvider implements CheckProvider {

    @Override
    public List<Check> provideChecks() {

        List<Check> checks = ExampleGroups.exampleGroups().stream()
                .map(group -> new ExampleCheck(group.getJiraName(), group, Collections.singletonList(ExampleTeam.BE)))
                .collect(Collectors.toList());

        checks.add(new FailingCheck("test", "message"));
        checks.add(new ExampleCheck("test", ExampleGroups.FIRST, Collections.singletonList(ExampleTeam.FE)).withLink("link ONE"));
        checks.add(new ExampleCheck("test", ExampleGroups.FIRST, Collections.singletonList(ExampleTeam.BE)).withLink("link TWO"));

        checks.add(new ExampleCheck("Green Check ONE", ExampleGroups.SECOND, Collections.singletonList(ExampleTeam.FE)).withState(State.GREEN));
        checks.add(new ExampleCheck("Green Check TWO", ExampleGroups.SECOND, Collections.singletonList(ExampleTeam.FE)).withState(State.GREEN));

        return checks;
    }
}
