package de.axelspringer.ideas.tools.dash.example;

import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.check.CheckExecutor;
import de.axelspringer.ideas.tools.dash.business.check.CheckResult;
import de.axelspringer.ideas.tools.dash.presentation.State;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ExampleCheckExecutor implements CheckExecutor<ExampleCheck> {

    @Override
    public List<CheckResult> executeCheck(ExampleCheck check) {
        return Collections.singletonList(new CheckResult(State.YELLOW, check.getName(), "info", 0, 1, check.getGroup()));
    }

    @Override
    public boolean isApplicable(Check check) {
        return check instanceof ExampleCheck;
    }
}
