package de.axelspringer.ideas.tools.dash.business.datadog;

import java.util.Map;

public class DataDogMonitorOptions {

    private Boolean notify_no_data;
    private Boolean notify_audit;
    private Boolean is_data_sparse;

    private Long renotify_interval;
    private Long no_data_timeframe;
    private Long timeout_h;
    private Long silenced_timeout_ts;

    private String escalation_message;
    private Map<String, Object> silenced;

    public boolean isSilenced() {
        if (silenced != null && !silenced.isEmpty()) {
            return true;
        }
        return false;
    }
}
