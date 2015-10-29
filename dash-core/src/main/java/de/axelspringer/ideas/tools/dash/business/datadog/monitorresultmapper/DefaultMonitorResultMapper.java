package de.axelspringer.ideas.tools.dash.business.datadog.monitorresultmapper;

import de.axelspringer.ideas.tools.dash.business.check.CheckResult;
import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.business.datadog.DataDogCheck;
import de.axelspringer.ideas.tools.dash.business.datadog.DataDogDowntimes;
import de.axelspringer.ideas.tools.dash.business.datadog.DataDogMonitor;
import de.axelspringer.ideas.tools.dash.presentation.State;

public class DefaultMonitorResultMapper implements MonitorResultMapper {

    public CheckResult mapToResult(DataDogMonitor monitor, DataDogCheck check, DataDogDowntimes downtimes) {
        final Group group = check.getGroup();
        final String infoMessage = decideInfoMessage(monitor, downtimes);
        final State state = decideState(monitor, downtimes);

        final CheckResult checkResult = new CheckResult(state, monitor.getName(), infoMessage, 1, State.GREEN == state ? 0 : 1, group);

        // HINT https://app.datadoghq.com/monitors#status?id=182437&group=all
        checkResult.withLink("https://app.datadoghq.com/monitors#status?id=" + monitor.getId() + "&group=all");
        return checkResult;
    }

    protected String decideInfoMessage(DataDogMonitor monitor, DataDogDowntimes downtimes) {
        if (monitor.isSilencedMonitor()) {
            return "NOT ACTIVE (silenced)!";
        } else if (downtimes.hasDowntime(monitor)) {
            return "MAINTENANCE!";
        } else {
            return monitor.getOverallState();
        }
    }

    protected State decideState(DataDogMonitor monitor, DataDogDowntimes downtimes) {
        return monitor.isTriggeredConsideringDowntimes(downtimes) ? State.RED : State.GREEN;
    }
}
