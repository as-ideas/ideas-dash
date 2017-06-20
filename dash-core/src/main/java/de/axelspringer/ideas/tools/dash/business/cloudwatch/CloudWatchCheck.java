package de.axelspringer.ideas.tools.dash.business.cloudwatch;

import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.business.customization.Team;

import java.util.List;

/**
 * Amazon Cloudwatch {@link Check}.
 */
public class CloudWatchCheck extends Check {

    private final String awsRegion;

    public CloudWatchCheck(
            String name,
            Group group,
            List<Team> teams,
            String awsRegion) {
        super(name, group, teams);
        this.awsRegion = awsRegion;
    }

    public String getAwsRegion() {
        return awsRegion;
    }

}
