package de.axelspringer.ideas.tools.dash.business.datadog;

import de.axelspringer.ideas.tools.dash.business.check.AbstractCheck;
import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.business.customization.Team;
import lombok.Getter;

@Getter
public class DataDogCheck extends AbstractCheck {

    private final String apiKey;

    private final String appKey;

    private final String nameFilter;

    public DataDogCheck(String name, Group group, Team team, String apiKey, String appKey, String nameFilter) {

        super(name, group, team);
        this.apiKey = apiKey;
        this.appKey = appKey;
        this.nameFilter = nameFilter;
    }
}
