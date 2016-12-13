package de.axelspringer.ideas.tools.dash.business.check;

import de.axelspringer.ideas.tools.dash.business.check.checkresult.CheckResult;
import de.axelspringer.ideas.tools.dash.presentation.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;


@Service
public class CheckService {

    private static final Logger LOG = LoggerFactory.getLogger(CheckService.class);

    @Autowired
    private List<CheckExecutor> checkExecutors;

    @Value("${dash.iconsEnabled:true}")
    private boolean iconsEnabled;

    private int numberOfParallelTask = 8;

    public List<CheckResult> check(List<Check> checks) {
        try {
            ForkJoinPool forkJoinPool = new ForkJoinPool(getNumberOfParallelTask());
            return forkJoinPool.submit(() ->
                    checks.parallelStream()
                            .map(this::executeCheck)
                            .flatMap(Collection::stream)
                            .collect(Collectors.toList())
            ).get();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    private List<CheckResult> executeCheck(Check check) {
        CheckExecutor checkExecutor = executor(check);
        try {
            List<CheckResult> checkResults = checkExecutor.executeCheck(check);
            return decorateCheckResults(check, checkResults);
        } catch (Exception e) {
            LOG.error("There are unhandled errors when performing check '{}' on stage '{}' for teams '{}'", check.getName(), check.getGroup(), check.getTeams());
            LOG.error(e.getMessage(), e);
            return Collections.singletonList(new CheckResult(State.RED, "unhandled check error: " + e.getMessage(), check.getName(), 0, 0, check.getGroup()).withTeams(check.getTeams()));
        }
    }

    private List<CheckResult> decorateCheckResults(Check check, List<CheckResult> results) {
        if (iconsEnabled) {
            results.forEach(checkResult -> {
                if (StringUtils.isEmpty(checkResult.getIconSrc())) {
                    checkResult.withIconSrc(check.getIconSrc());
                }
            });
        }
        return results;
    }

    @SuppressWarnings("unchecked")
    CheckExecutor executor(Check check) {

        final ArrayList<CheckExecutor> applicableExecutors = new ArrayList<>(checkExecutors);
        applicableExecutors.removeIf(checkExecutor -> !checkExecutor.isApplicable(check));

        if (applicableExecutors.size() != 1) {
            LOG.error("{} executors found for check of type {}", applicableExecutors.size(), check.getClass().getName());
            throw new RuntimeException("executor count mismatch (no executor? too many executors?)");
        }

        return applicableExecutors.get(0);
    }

    /**
     * @return the number of parallel checks to execute
     */
    public int getNumberOfParallelTask() {
        return numberOfParallelTask;
    }


    /**
     * @param numberOfParallelTask the number of parallel checks to execute
     */
    public void setNumberOfParallelTask(int numberOfParallelTask) {
        this.numberOfParallelTask = numberOfParallelTask;
    }
}