package de.axelspringer.ideas.tools.dash.business.fabric;

/**
 * DTO to wrap {@link FabricApp} with the corresponding crashes count
 */
public class FabricAppWithProblems {

    private final FabricApp fabricApp;
    private final Integer warningCount;
    private final Integer severeCount;

    public FabricAppWithProblems(FabricApp fabricApp, Integer warningCount, Integer severeCount) {
        this.fabricApp = fabricApp;
        this.warningCount = warningCount;
        this.severeCount = severeCount;
    }

    public FabricApp getFabricApp() {
        return fabricApp;
    }

    public Integer getWarningCount() {
        return warningCount;
    }

    public Integer getSevereCount() {
        return severeCount;
    }
}
