package de.axelspringer.ideas.tools.dash.business.cloudwatch;

import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.business.customization.Team;

import java.util.List;

/**
 * Amazon Cloudwatch {@link Check}.
 */
public class CloudWatchCheck extends Check {

    private final String awsAccessKeyId;
    private final String awsSecretKey;

    public CloudWatchCheck(
            String name,
            Group group,
            List<Team> teams,
            String awsAccessKeyId,
            String awsSecretKey) {
        super(name, group, teams);
        this.awsAccessKeyId = awsAccessKeyId;
        this.awsSecretKey = awsSecretKey;
    }

    public String getAwsAccessKeyId() {
        return awsAccessKeyId;
    }

    public String getAwsSecretKey() {
        return awsSecretKey;
    }
}
