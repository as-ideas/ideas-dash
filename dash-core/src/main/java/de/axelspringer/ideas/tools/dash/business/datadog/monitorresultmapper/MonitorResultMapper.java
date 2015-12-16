package de.axelspringer.ideas.tools.dash.business.datadog.monitorresultmapper;

import de.axelspringer.ideas.tools.dash.business.check.CheckResult;
import de.axelspringer.ideas.tools.dash.business.datadog.DataDogCheck;
import de.axelspringer.ideas.tools.dash.business.datadog.DataDogDowntimes;
import de.axelspringer.ideas.tools.dash.business.datadog.DataDogMonitor;

public interface MonitorResultMapper {

    CheckResult mapToResult(DataDogMonitor monitor, DataDogCheck check, DataDogDowntimes downtimes);
}
