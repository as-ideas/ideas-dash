package de.axelspringer.ideas.tools.dash.business.fabric;

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

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FabricApp)) {
            return false;
        }
        final FabricApp other = (FabricApp) o;
        if (!other.canEqual((Object) this)) {
            return false;
        }
        final Object this$id = this.id;
        final Object other$id = other.id;
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) {
            return false;
        }
        final Object this$name = this.name;
        final Object other$name = other.name;
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        final Object this$icon_url = this.icon_url;
        final Object other$icon_url = other.icon_url;
        if (this$icon_url == null ? other$icon_url != null : !this$icon_url.equals(other$icon_url)) {
            return false;
        }
        final Object this$impacted_devices_count = this.impacted_devices_count;
        final Object other$impacted_devices_count = other.impacted_devices_count;
        if (this$impacted_devices_count == null ? other$impacted_devices_count != null : !this$impacted_devices_count.equals(other$impacted_devices_count)) {
            return false;
        }
        final Object this$unresolved_issues_count = this.unresolved_issues_count;
        final Object other$unresolved_issues_count = other.unresolved_issues_count;
        if (this$unresolved_issues_count == null ? other$unresolved_issues_count != null : !this$unresolved_issues_count.equals(other$unresolved_issues_count)) {
            return false;
        }
        final Object this$crashes_count = this.crashes_count;
        final Object other$crashes_count = other.crashes_count;
        if (this$crashes_count == null ? other$crashes_count != null : !this$crashes_count.equals(other$crashes_count)) {
            return false;
        }
        final Object this$dashboard_url = this.dashboard_url;
        final Object other$dashboard_url = other.dashboard_url;
        if (this$dashboard_url == null ? other$dashboard_url != null : !this$dashboard_url.equals(other$dashboard_url)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.id;
        result = result * PRIME + ($id == null ? 0 : $id.hashCode());
        final Object $name = this.name;
        result = result * PRIME + ($name == null ? 0 : $name.hashCode());
        final Object $icon_url = this.icon_url;
        result = result * PRIME + ($icon_url == null ? 0 : $icon_url.hashCode());
        final Object $impacted_devices_count = this.impacted_devices_count;
        result = result * PRIME + ($impacted_devices_count == null ? 0 : $impacted_devices_count.hashCode());
        final Object $unresolved_issues_count = this.unresolved_issues_count;
        result = result * PRIME + ($unresolved_issues_count == null ? 0 : $unresolved_issues_count.hashCode());
        final Object $crashes_count = this.crashes_count;
        result = result * PRIME + ($crashes_count == null ? 0 : $crashes_count.hashCode());
        final Object $dashboard_url = this.dashboard_url;
        result = result * PRIME + ($dashboard_url == null ? 0 : $dashboard_url.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof FabricApp;
    }

    public String toString() {
        return "de.axelspringer.ideas.tools.dash.business.fabric.FabricApp(id=" + this.id + ", name=" + this.name + ", icon_url=" + this.icon_url + ", impacted_devices_count=" + this.impacted_devices_count + ", unresolved_issues_count=" + this.unresolved_issues_count + ", crashes_count=" + this.crashes_count + ", dashboard_url=" + this.dashboard_url + ")";
    }
}
