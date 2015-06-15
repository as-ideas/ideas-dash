package de.axelspringer.ideas.tools.dash.presentation;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

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
