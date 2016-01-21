package de.axelspringer.ideas.tools.dash.business.datadog;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.axelspringer.ideas.tools.dash.business.check.AbstractCheck;
import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.business.customization.Team;

public class DataDogCheck extends AbstractCheck {

    private static final String ICON_SRC = "assets/datadog-logo.png";
    private final String apiKey;

    private final String appKey;

    private final String nameFilter;
    private Map<String, List<Team>> jobNameTeamMappings = new HashMap<>();

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

    public Map<String, List<Team>> getJobNameTeamMappings() {
        return Collections.unmodifiableMap(jobNameTeamMappings);
    }

    public DataDogCheck withJobNameTeamMapping(String jobName, List<Team> teams) {
        jobNameTeamMappings.put(jobName, teams);
        return this;
    }
}
