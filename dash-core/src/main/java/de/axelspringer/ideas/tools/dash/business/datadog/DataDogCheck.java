package de.axelspringer.ideas.tools.dash.business.datadog;

import de.axelspringer.ideas.tools.dash.business.check.AbstractCheck;
import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.business.customization.Team;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DataDogCheck extends AbstractCheck {

    private static final String ICON_SRC = "http://en.community.dell.com/cfs-file.ashx/__key/communityserver-blogs-components-weblogfiles/00-00-00-37-45/1817.DATADOG.png";
    private final String apiKey;

    private final String appKey;

    private final String nameFilter;
    private Map<String, Team> jobNameTeamMappings = new HashMap<>();

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

    public Map<String, Team> getJobNameTeamMappings() {
        return Collections.unmodifiableMap(jobNameTeamMappings);
    }

    public DataDogCheck withJobNameTeamMapping(String jobName, Team team) {
        jobNameTeamMappings.put(jobName, team);
        return this;
    }
}
