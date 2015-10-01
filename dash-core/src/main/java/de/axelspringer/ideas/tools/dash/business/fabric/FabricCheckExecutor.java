package de.axelspringer.ideas.tools.dash.business.fabric;

import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.check.CheckExecutor;
import de.axelspringer.ideas.tools.dash.business.check.CheckResult;
import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.business.customization.Team;
import de.axelspringer.ideas.tools.dash.presentation.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Executor for {@link FabricCheck}
 */
@Service
public class FabricCheckExecutor implements CheckExecutor<FabricCheck> {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private FabricService fabricService;

    @Override
    public List<CheckResult> executeCheck(FabricCheck fabricCheck) {

        final Team team = fabricCheck.getTeam();
        final Group group = fabricCheck.getGroup();

        // log-in
        final FabricAuth fabricAuth;
        try {
            fabricAuth = fabricService.logIn(fabricCheck.getEmail(), fabricCheck.getPassword());
        } catch (FabricExecutionException e) {
            return Collections.singletonList(new CheckResult(State.RED, "FABRIC", e.getMessage(), 1, 1, group).withTeam(team));
        }

        // load apps
        final List<FabricApp> fabricApps;
        try {
            fabricApps = fabricService.loadApps(fabricAuth);
        } catch (FabricExecutionException e) {
            return Collections.singletonList(new CheckResult(State.RED, "FABRIC", e.getMessage(), 1, 1, group).withTeam(team));
        }

        return fabricApps.stream()
                .map(fabricApp -> issues(fabricAuth, fabricApp))
                .map(fabricAppWithProblems -> mapFabricAppWithProblemsToCheckResult(fabricAppWithProblems, group, team))
                .collect(Collectors.toList());
    }

    /**
     * utilizes {@link FabricService#loadIssues(String, FabricAuth)} and creates the {@link FabricAppWithProblems} - wrapper
     *
     * @param fabricAuth
     * @param fabricApp
     * @return
     */
    private FabricAppWithProblems issues(FabricAuth fabricAuth, FabricApp fabricApp) {

        final List<FabricIssue> fabricIssues = fabricService.loadIssues(fabricApp.getId(), fabricAuth);

        int warningCount = 0;
        int severeCount = 0;

        for (FabricIssue fabricIssue : fabricIssues) {
            if (!ignoreIssue(fabricIssue, fabricApp.getId(), fabricAuth)) {
                if (fabricIssue.getEvent_type() == FabricIssue.EVENT_TYPE_SEVERE) {
                    severeCount++;
                } else {
                    warningCount++;
                }
            }
        }

        return new FabricAppWithProblems(fabricApp, warningCount, severeCount);
    }

    private CheckResult mapFabricAppWithProblemsToCheckResult(FabricAppWithProblems fabricAppWithProblems, Group group, Team team) {

        final FabricApp fabricApp = fabricAppWithProblems.getFabricApp();
        final Integer problemCount = fabricAppWithProblems.getWarningCount() + fabricAppWithProblems.getSevereCount();

        final State state = problemCount < 1 ? State.GREEN : fabricAppWithProblems.getSevereCount() > 0 ? State.RED : State.YELLOW;
        return new CheckResult(state, fabricApp.getName(), problemCount + " unresolved", 1 + problemCount, problemCount, group)
                .withLink(fabricApp.getDashboard_url())
                .withTeam(team);
    }

    private boolean ignoreIssue(FabricIssue fabricIssue, String fabricAppId, FabricAuth fabricAuth) {

        return fabricIssue.getNotes_count() > 1
                && fabricService.loadNotes(fabricAppId, fabricIssue.getId(), fabricAuth).stream().anyMatch(note -> note.getBody().contains("#ignoreIssue"));
    }

    @Override
    public boolean isApplicable(Check check) {
        return check instanceof FabricCheck;
    }
}