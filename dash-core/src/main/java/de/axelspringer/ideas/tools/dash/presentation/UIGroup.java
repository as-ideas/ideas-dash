package de.axelspringer.ideas.tools.dash.presentation;

import de.axelspringer.ideas.tools.dash.business.check.CheckResult;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class UIGroup {

    private String name;

    private String info;

    private State state;

    private int totalCount = 0;

    private int failCount = 0;

    private List<CheckResult> checks = new ArrayList<>();

    private int orderScore = -1;

    public void add(CheckResult checkResult) {
        checks.addAll(Arrays.asList(checkResult));
    }

    public void add(List<CheckResult> checkResults) {
        checks.addAll(checkResults);
    }
}
