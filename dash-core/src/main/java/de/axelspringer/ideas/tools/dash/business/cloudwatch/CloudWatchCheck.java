package de.axelspringer.ideas.tools.dash.business.cloudwatch;

import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.business.customization.Team;

import java.util.List;

/**
 * Amazon Cloudwatch {@link Check}.
 */
public class CloudWatchCheck extends Check {

    private final AmazonCloudWatch cloudWatch;
    private final String region;

    public CloudWatchCheck(
            String name,
            Group group,
            List<Team> teams,
            String awsRegion) {
        super(name, group, teams);
        this.region = awsRegion;
        this.cloudWatch = AmazonCloudWatchClientBuilder
                .standard()
                .withRegion(awsRegion)
                .build();;
    }

    public AmazonCloudWatch getCloudWatch() {
        return this.cloudWatch;
    }

    public String getRegion() {
        return region;
    }
}
