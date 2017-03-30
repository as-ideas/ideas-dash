package de.axelspringer.ideas.tools.dash.business.cloudwatch;

import com.amazonaws.services.cloudwatch.model.DescribeAlarmsResult;
import com.amazonaws.services.cloudwatch.model.MetricAlarm;
import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.check.CheckExecutor;
import de.axelspringer.ideas.tools.dash.business.check.checkresult.CheckResult;
import de.axelspringer.ideas.tools.dash.presentation.State;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * {@link CheckExecutor} for {@link CloudWatchCheck}.
 * @author Christian Heike
 */
@Service
public class CloudWatchCheckExecutor implements CheckExecutor<CloudWatchCheck> {

    @Override
    public List<CheckResult> executeCheck(final CloudWatchCheck check) {
        final DescribeAlarmsResult alarms = check.getAmazonCloudwatchClient().describeAlarms(check.getAlarmDescription());
        return alarms.getMetricAlarms().parallelStream().map(v -> factorCheckResult(v, check)).collect(Collectors.toList());
    }

    private CheckResult factorCheckResult(final MetricAlarm metricAlarm, final CloudWatchCheck check) {
        final State state = check.getStateMapper().apply(metricAlarm.getStateValue());
        final String name = metricAlarm.getAlarmName();
        final String info = metricAlarm.getAlarmDescription();
        return new CheckResult(state, name, info, 1, state == State.GREEN ? 0 : 1, check.getGroup());
    }

    @Override
    public boolean isApplicable(final Check check) {
        return check instanceof CloudWatchCheck;
    }

}
