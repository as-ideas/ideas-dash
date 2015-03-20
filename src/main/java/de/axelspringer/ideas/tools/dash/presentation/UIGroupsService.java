package de.axelspringer.ideas.tools.dash.presentation;

import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.check.CheckProvider;
import de.axelspringer.ideas.tools.dash.business.check.CheckResult;
import de.axelspringer.ideas.tools.dash.business.check.CheckService;
import de.axelspringer.ideas.tools.dash.business.customization.Stage;
import de.axelspringer.ideas.tools.dash.business.customization.StageProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class UIGroupsService {

    // begin with an empty object. checks shall only be executed by the scheduler
    private UIGroups uiGroups = new UIGroups();

    @Autowired
    private List<CheckProvider> checkProviders;

    @Autowired
    private CheckService checkService;

    @Autowired
    private StageProvider stageProvider;

    @Value("${checks.ignore.stages}")
    private String[] excludeChecksForStages = new String[0];

    public UIGroups groups() {
        return uiGroups;
    }

    @Scheduled(fixedDelay = 120000)
    public void check() {

        // checks to perform
        List<Check> allChecks = excludeChecksForStages(allChecks());

        // execute them
        List<CheckResult> checkResults = checkService.check(allChecks);

        // map checkresults to groups
        Map<Stage, List<CheckResult>> checkResultsMappedToGroup = mapResultsToGroups(checkResults);

        // build uigroup-object and set field
        this.uiGroups = buildUiGroups(checkResultsMappedToGroup);
    }

    private UIGroups buildUiGroups(Map<Stage, List<CheckResult>> checkResultsMappedToGroup) {

        UIGroups newUiGroups = new UIGroups();

        newUiGroups.setLastUpdateTime(LocalDateTime.now().toString());

        for (Stage group : checkResultsMappedToGroup.keySet()) {

            UIGroup uiGroup = new UIGroup();
            uiGroup.setName(group.toString());

            List<CheckResult> checkResultsForGroup = checkResultsMappedToGroup.get(group);

            Integer testCount = 0;
            Integer failCount = 0;
            State state = null;
            for (CheckResult checkResult : checkResultsForGroup) {
                testCount += checkResult.getTestCount();
                failCount += checkResult.getFailCount();
                state = aggregate(state, checkResult.getState());
                uiGroup.add(checkResult);
            }
            uiGroup.setTotalCount(testCount);
            uiGroup.setFailCount(failCount);

            String info = failCount > 0 ? failCount + "/" + testCount : testCount.toString();
            uiGroup.setInfo(info);
            uiGroup.setState(state);

            newUiGroups.add(uiGroup);
        }
        return newUiGroups;
    }

    State aggregate(State state1, State state2) {

        if (state1 == null) {
            return state2;
        }

        if (state2 == null) {
            return state1;
        }

        if (state1 == State.RED || state2 == State.RED) {
            return State.RED;
        }
        if (state1 == State.YELLOW || state2 == State.YELLOW) {
            return State.YELLOW;
        }
        if (state1 == State.GREY || state2 == State.GREY) {
            return State.GREY;
        }

        if (state1 == State.GREEN || state2 == State.GREEN) {
            return State.GREEN;
        }
        return State.GREY;
    }

    private Map<Stage, List<CheckResult>> mapResultsToGroups(List<CheckResult> checkResults) {

        Map<Stage, List<CheckResult>> checkResultsMappedToGroup = new HashMap<>();
        for (CheckResult checkResult : checkResults) {

            Stage checkGroup = checkResult.getStage();
            if (!checkResultsMappedToGroup.containsKey(checkGroup)) {
                checkResultsMappedToGroup.put(checkGroup, new ArrayList<>());
            }
            List<CheckResult> checkResultsForGroup = checkResultsMappedToGroup.get(checkGroup);
            checkResultsForGroup.add(checkResult);
        }
        return checkResultsMappedToGroup;
    }

    private List<Check> allChecks() {

        List<Check> allChecks = new ArrayList<>();
        for (CheckProvider checkProvider : checkProviders) {
            allChecks.addAll(checkProvider.provideChecks());
        }
        return allChecks;
    }

    private List<Check> excludeChecksForStages(List<Check> checks) {
        List<Check> checksToExecute = new ArrayList<>(checks);
        if (excludeChecksForStages == null || excludeChecksForStages.length < 1) {
            return checksToExecute;
        }

        final Set<Stage> excludeGroups = getExcludeGroups();
        checks.stream().filter(check -> excludeGroups.contains(check.getStage())).forEach(checksToExecute::remove);
        return checksToExecute;
    }

    public Set<Stage> getExcludeGroups() {
        Set<Stage> excludeGroups = new HashSet<>();
        for (Stage group : stageProvider.getStages()) {
            for (String stageId : excludeChecksForStages) {
                if (group.getStageId().equalsIgnoreCase(stageId)) {
                    excludeGroups.add(group);
                    break;
                }
            }
        }
        return excludeGroups;
    }
}
