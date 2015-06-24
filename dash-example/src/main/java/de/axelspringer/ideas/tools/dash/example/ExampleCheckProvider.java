package de.axelspringer.ideas.tools.dash.example;

import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.check.CheckProvider;
import de.axelspringer.ideas.tools.dash.business.failure.FailingCheck;
import de.axelspringer.ideas.tools.dash.presentation.State;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ExampleCheckProvider implements CheckProvider {

    @Override
    public List<Check> provideChecks() {

        List<Check> checks = new ArrayList<>();

        for (ExampleGroup group : ExampleGroup.values()) {
            checks.add(new ExampleCheck(group.getJiraName(), group, ExampleTeamProvider.EXAMPLE_TEAM));
        }

        checks.add(new FailingCheck("test", "message"));
        checks.add(new ExampleCheck("test", ExampleGroup.EXPECTED_FIRST, ExampleTeamProvider.EXAMPLE_TEAM).withLink("link ONE"));
        checks.add(new ExampleCheck("test", ExampleGroup.EXPECTED_FIRST, ExampleTeamProvider.EXAMPLE_TEAM).withLink("link TWO"));

        checks.add(new ExampleCheck("Green Check ONE", ExampleGroup.EXPECTED_SECOND, ExampleTeamProvider.EXAMPLE_TEAM).withState(State.GREEN));
        checks.add(new ExampleCheck("Green Check TWO", ExampleGroup.EXPECTED_SECOND, ExampleTeamProvider.EXAMPLE_TEAM).withState(State.GREEN));

        return checks;
    }
}
