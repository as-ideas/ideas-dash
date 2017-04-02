package de.axelspringer.ideas.tools.dash.business.cloudwatch;

import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import com.amazonaws.services.cloudwatch.model.DescribeAlarmsRequest;
import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.business.customization.Team;

import java.util.List;

/**
 * Amazon Cloudwatch {@link Check}.
 */
public class CloudWatchCheck extends Check {

    private final AmazonCloudWatch amazonCloudwatchClient;
    private final DescribeAlarmsRequest alarmDescription;

    public CloudWatchCheck(final String name, final Group group, final List<Team> teams,
                           final AmazonCloudWatchClientBuilder builder, final DescribeAlarmsRequest alarmDescription) {
        super(name, group, teams);
        this.amazonCloudwatchClient = builder.build();
        this.alarmDescription = alarmDescription;
    }

    public AmazonCloudWatch getAmazonCloudwatchClient() {
        return amazonCloudwatchClient;
    }

    public DescribeAlarmsRequest getAlarmDescription() {
        return alarmDescription;
    }
}
