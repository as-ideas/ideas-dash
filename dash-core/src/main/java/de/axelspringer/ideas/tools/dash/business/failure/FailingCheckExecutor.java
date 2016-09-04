package de.axelspringer.ideas.tools.dash.business.failure;

import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.check.CheckExecutor;
import de.axelspringer.ideas.tools.dash.business.check.checkresult.CheckResult;
import de.axelspringer.ideas.tools.dash.presentation.State;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class FailingCheckExecutor implements CheckExecutor<FailingCheck> {


    @Override
    public List<CheckResult> executeCheck(FailingCheck check) {
        return Collections.singletonList(new CheckResult(State.RED, check.getName(), check.getFailureMessage(), 1, 1, check.getGroup()));
    }

    @Override
    public boolean isApplicable(Check check) {
        return check instanceof FailingCheck;
    }
}
