package de.axelspringer.ideas.tools.dash.business.datadog;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DataDogMonitorOptions {

    @JsonProperty("notify_no_data")
    private Boolean notifyNoData;

    @JsonProperty("notify_audit")
    private Boolean notifyAudit;

    @JsonProperty("is_data_sparse")
    private Boolean isDataSparse;

    @JsonProperty("renotify_interval")
    private Long renotifyInterval;

    @JsonProperty("no_data_timeframe")
    private Long noDataTimeframe;

    @JsonProperty("timeout_h")
    private Long timeoutH;

    @JsonProperty("silenced_timeout_ts")
    private Long silencedTimeoutTs;

    @JsonProperty("escalation_message")
    private String escalationMessage;

    private Map<String, Object> silenced;

    public boolean isSilenced() {
        if (silenced != null && !silenced.isEmpty()) {
            return true;
        }
        return false;
    }

    public boolean isNotifyNoData() {
        return (notifyNoData != null && notifyNoData);
    }
}
