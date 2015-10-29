package de.axelspringer.ideas.tools.dash.business.datadog;

import de.axelspringer.ideas.tools.dash.business.check.AbstractCheck;
import de.axelspringer.ideas.tools.dash.business.customization.Group;

public class DataDogCheck extends AbstractCheck {

    private static final String ICON_SRC = "assets/datadog-logo.png";
    private final String apiKey;

    private final String appKey;

    private final String nameFilter;

    public DataDogCheck(String name, Group group, String apiKey, String appKey, String nameFilter) {

        super(name, group, null);
        this.apiKey = apiKey;
        this.appKey = appKey;
        this.nameFilter = nameFilter;
    }

    public String getApiKey() {
        return this.apiKey;
    }

    public String getAppKey() {
        return this.appKey;
    }

    public String getNameFilter() {
        return this.nameFilter;
    }

    @Override
    public String getIconSrc() {
        return ICON_SRC;
    }
}
