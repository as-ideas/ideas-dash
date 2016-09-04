package de.axelspringer.ideas.tools.dash.business.pingdom;

import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.business.customization.Team;

import java.util.regex.Pattern;

public class PingdomCheck extends Check {

    private final PingdomConfig githubConfig;

    private String regexForMatchingNames = "";

    private Pattern pattern;

    public PingdomCheck(String name, Group group, Team team, PingdomConfig githubConfig, String regexForMatchingNames) {
        super(name, group, null);
        this.githubConfig = githubConfig;
        this.regexForMatchingNames = regexForMatchingNames;
    }

    public PingdomConfig pingdomConfig() {
        return githubConfig;
    }

    public String regexForMatchingRepoNames() {
        return regexForMatchingNames;
    }

    public Pattern pattern() {
        if (pattern == null) {
            pattern = Pattern.compile(regexForMatchingNames);
        }
        return pattern;
    }


    @Override
    public String getIconSrc() {
        return "assets/pingdom-logo.png";
    }

}
