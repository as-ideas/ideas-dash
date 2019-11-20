package de.axelspringer.ideas.tools.dash.business.cloudwatch;

import com.amazonaws.services.cloudwatch.model.MetricAlarm;
import de.axelspringer.ideas.tools.dash.business.check.checkresult.CheckResult;

/**
 * Decorates the CheckResult for the UI based on the check configuration and corresponding cloudwatch alarm
 */
public interface CloudWatchResultDecorator {

    /**
     * @return the decorated check result or null if the check result should be excluded
     */
    default CheckResult decorate(CheckResult checkResult, CloudWatchCheck check, MetricAlarm alarm) {
        return checkResult;
    };
}
