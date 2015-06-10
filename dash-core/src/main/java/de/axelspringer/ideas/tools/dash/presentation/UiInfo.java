package de.axelspringer.ideas.tools.dash.presentation;

import java.util.ArrayList;
import java.util.List;

public class UiInfo {

    private final String applicationStartId;
    private String lastUpdateTime = "N/A";
    private List<UIGroup> groups = new ArrayList<>();

    public UiInfo(String applicationStartId) {
        this.applicationStartId = applicationStartId;
    }

    public void add(UIGroup uiGroup) {
        groups.add(uiGroup);
    }

    public String getApplicationStartId() {
        return this.applicationStartId;
    }

    public String getLastUpdateTime() {
        return this.lastUpdateTime;
    }

    public List<UIGroup> getGroups() {
        return this.groups;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public void setGroups(List<UIGroup> groups) {
        this.groups = groups;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UiInfo)) {
            return false;
        }
        final UiInfo other = (UiInfo) o;
        if (!other.canEqual((Object) this)) {
            return false;
        }
        final Object this$applicationStartId = this.applicationStartId;
        final Object other$applicationStartId = other.applicationStartId;
        if (this$applicationStartId == null ? other$applicationStartId != null : !this$applicationStartId.equals(other$applicationStartId)) {
            return false;
        }
        final Object this$lastUpdateTime = this.lastUpdateTime;
        final Object other$lastUpdateTime = other.lastUpdateTime;
        if (this$lastUpdateTime == null ? other$lastUpdateTime != null : !this$lastUpdateTime.equals(other$lastUpdateTime)) {
            return false;
        }
        final Object this$groups = this.groups;
        final Object other$groups = other.groups;
        if (this$groups == null ? other$groups != null : !this$groups.equals(other$groups)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $applicationStartId = this.applicationStartId;
        result = result * PRIME + ($applicationStartId == null ? 0 : $applicationStartId.hashCode());
        final Object $lastUpdateTime = this.lastUpdateTime;
        result = result * PRIME + ($lastUpdateTime == null ? 0 : $lastUpdateTime.hashCode());
        final Object $groups = this.groups;
        result = result * PRIME + ($groups == null ? 0 : $groups.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof UiInfo;
    }

    public String toString() {
        return "de.axelspringer.ideas.tools.dash.presentation.UiInfo(applicationStartId=" + this.applicationStartId + ", lastUpdateTime=" + this.lastUpdateTime + ", groups=" + this.groups + ")";
    }
}
