package de.axelspringer.ideas.tools.dash.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;
import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.check.CheckProvider;
import de.axelspringer.ideas.tools.dash.business.failure.FailingCheck;
import de.axelspringer.ideas.tools.dash.presentation.State;

@Component
public class ExampleCheckProvider implements CheckProvider {

    @Override
    public List<Check> provideChecks() {

        List<Check> checks = new ArrayList<>();

        for (ExampleGroup group : ExampleGroup.values()) {
            checks.add(new ExampleCheck(group.getJiraName(), group, Arrays.asList(ExampleTeam.BE)));
        }

        checks.add(new FailingCheck("test", "message"));
        checks.add(new ExampleCheck("test", ExampleGroup.EXPECTED_FIRST, Arrays.asList(ExampleTeam.FE)).withLink("link ONE"));
        checks.add(new ExampleCheck("test", ExampleGroup.EXPECTED_FIRST, Arrays.asList(ExampleTeam.BE)).withLink("link TWO"));

        checks.add(new ExampleCheck("Green Check ONE", ExampleGroup.EXPECTED_SECOND, Arrays.asList(ExampleTeam.FE)).withState(State.GREEN));
        checks.add(new ExampleCheck("Green Check TWO", ExampleGroup.EXPECTED_SECOND, Arrays.asList(ExampleTeam.FE)).withState(State.GREEN));

        return checks;
    }
}
