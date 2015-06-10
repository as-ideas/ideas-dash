package de.axelspringer.ideas.tools.dash.business.fabric;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * DTO for fabric app-info-endpoint
 */
public class FabricApp {

    private String id;
    private String name;
    private String icon_url;
    private Integer impacted_devices_count;
    private Integer unresolved_issues_count;
    private Integer crashes_count;
    private String dashboard_url;

    public FabricApp() {
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getIcon_url() {
        return this.icon_url;
    }

    public Integer getImpacted_devices_count() {
        return this.impacted_devices_count;
    }

    public Integer getUnresolved_issues_count() {
        return this.unresolved_issues_count;
    }

    public Integer getCrashes_count() {
        return this.crashes_count;
    }

    public String getDashboard_url() {
        return this.dashboard_url;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    public void setImpacted_devices_count(Integer impacted_devices_count) {
        this.impacted_devices_count = impacted_devices_count;
    }

    public void setUnresolved_issues_count(Integer unresolved_issues_count) {
        this.unresolved_issues_count = unresolved_issues_count;
    }

    public void setCrashes_count(Integer crashes_count) {
        this.crashes_count = crashes_count;
    }

    public void setDashboard_url(String dashboard_url) {
        this.dashboard_url = dashboard_url;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
