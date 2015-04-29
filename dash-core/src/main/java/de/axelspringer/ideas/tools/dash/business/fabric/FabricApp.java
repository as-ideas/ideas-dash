package de.axelspringer.ideas.tools.dash.business.fabric;

import lombok.Data;

/**
 * DTO for fabric app-info-endpoint
 */
@Data
public class FabricApp {

    private String id;
    private String name;
    private String icon_url;
    private Integer impacted_devices_count;
    private Integer unresolved_issues_count;
    private Integer crashes_count;
    private String dashboard_url;
}
