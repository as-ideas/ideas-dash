package de.axelspringer.ideas.tools.dash.business.cloudwatch;

import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import com.amazonaws.services.cloudwatch.model.DescribeAlarmsRequest;
import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.business.customization.Team;
import de.axelspringer.ideas.tools.dash.presentation.State;

import java.util.List;
import java.util.function.Function;

/**
 * Amazon Cloudwatch {@link Check}.
 * @author Christian Heike
 */
public class CloudWatchCheck extends Check {

    private final AmazonCloudWatch amazonCloudwatchClient;
    private final DescribeAlarmsRequest alarmDescription;
    private final Function<String, State> stateMapper;

    public CloudWatchCheck(final String name, final Group group, final List<Team> teams,
                           final AmazonCloudWatchClientBuilder builder, final DescribeAlarmsRequest alarmDescription) {
        this(name, group, teams, builder, alarmDescription, CloudWatchCheck::mapState);
    }

    public CloudWatchCheck(final String name, final Group group, final List<Team> teams,
                           final AmazonCloudWatchClientBuilder builder, final DescribeAlarmsRequest alarmDescription,
                           final Function<String, State> stateMapper) {
        super(name, group, teams);
        this.amazonCloudwatchClient = builder.build();
        this.alarmDescription = alarmDescription;
        this.stateMapper = stateMapper;
    }

    public AmazonCloudWatch getAmazonCloudwatchClient() {
        return amazonCloudwatchClient;
    }

    public DescribeAlarmsRequest getAlarmDescription() {
        return alarmDescription;
    }

    public Function<String, State> getStateMapper() {
        return stateMapper;
    }

    public static State mapState(final String stateValue) {
        if (stateValue == null) {
            return State.GREY;
        }
        switch (stateValue) {
            case "OK":
                return State.GREEN;
            case "INSUFFICIENT_DATA":
                return State.YELLOW;
            case "ALARM":
                return State.RED;
            default:
                return State.GREY;
        }

    }

}
