package de.axelspringer.ideas.tools.dash.example;

import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.check.CheckExecutor;
import de.axelspringer.ideas.tools.dash.business.check.CheckResult;
import de.axelspringer.ideas.tools.dash.presentation.State;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class ExampleCheckExecutor implements CheckExecutor<ExampleCheck> {

    @Override
    public List<CheckResult> executeCheck(ExampleCheck check) {

        final CheckResult result = new CheckResult(State.YELLOW, check.getName(), "info", 0, 1, check.getGroup());
        if (new Random().nextInt() % 2 == 0) {
            result.markRunning();
        }
        return Arrays.asList(result, result, result);
    }

    @Override
    public boolean isApplicable(Check check) {
        return check instanceof ExampleCheck;
    }
}
