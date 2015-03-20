package de.axelspringer.ideas.tools.dash.business.check;

import de.axelspringer.ideas.tools.dash.presentation.State;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class CheckService {

    @Autowired
    private List<CheckExecutor> checkExecutors;

    public List<CheckResult> check(List<Check> checks) {

        return checks.parallelStream()
                // execute checks
                .map(check -> {
                    CheckExecutor checkExecutor = executor(check);
                    try {
                        return checkExecutor.executeCheck(check);
                    } catch (Exception e) {
                        log.error("There are unhandled errors when performing check '{}' on stage '{}' for team '{}'", check.getName(), check.getStage(), check.getTeam());
                        return Collections.singletonList(new CheckResult(State.RED, "unhandled check error", check.getName(), 0, 0, check.getStage()));
                    }
                })
                        // flatten (List<List<CheckResult>> -> List<CheckResult>
                .flatMap(Collection::stream)
                        // collect
                .collect(Collectors.toList());
    }

    CheckExecutor executor(Check check) {

        final ArrayList<CheckExecutor> applicableExecutors = new ArrayList<>(checkExecutors);
        applicableExecutors.removeIf(checkExecutor -> !checkExecutor.isApplicable(check));

        if (applicableExecutors.size() != 1) {
            log.error("{} executors found for check of type {}", applicableExecutors.size(), check.getClass().getName());
            throw new RuntimeException("executor count mismatch (no executor? too many executors?)");
        }

        return applicableExecutors.get(0);
    }
}