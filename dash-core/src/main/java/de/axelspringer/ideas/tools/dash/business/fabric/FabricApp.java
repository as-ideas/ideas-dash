package de.axelspringer.ideas.tools.dash.business.fabric;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * DTO for fabric app-info-endpoint
 */
public class FabricApp {

    private String id;
    private String name;
    private String icon_url;

    private String dashboard_url;

    public FabricApp() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon_url() {
        return this.icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    public String getDashboard_url() {
        return this.dashboard_url;
    }

    public void setDashboard_url(String dashboard_url) {
        this.dashboard_url = dashboard_url;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
