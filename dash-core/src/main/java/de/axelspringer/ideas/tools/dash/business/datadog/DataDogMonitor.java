package de.axelspringer.ideas.tools.dash.business.datadog;

import lombok.Data;

@Data
public class DataDogMonitor {

    public final static String STATE_OK = "OK";

    private String name;
    private String query;
    private String overall_state;
    private String type;
}
