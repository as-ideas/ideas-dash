package de.axelspringer.ideas.tools.dash.business.fabric;

/**
 * DTO to wrap {@link FabricApp} with the corresponding crashes count
 */
public class FabricAppWithCrashesCount {

    private final FabricApp fabricApp;
    private final Integer crashesCount;

    public FabricAppWithCrashesCount(FabricApp fabricApp, Integer crashesCount) {
        this.fabricApp = fabricApp;
        this.crashesCount = crashesCount;
    }

    public FabricApp getFabricApp() {
        return fabricApp;
    }

    public Integer getCrashesCount() {
        return crashesCount;
    }
}
