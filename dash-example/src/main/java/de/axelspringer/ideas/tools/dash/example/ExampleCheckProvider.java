package de.axelspringer.ideas.tools.dash.example;

import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.check.CheckProvider;
import de.axelspringer.ideas.tools.dash.business.failure.FailingCheck;
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

        return checks;
    }
}
