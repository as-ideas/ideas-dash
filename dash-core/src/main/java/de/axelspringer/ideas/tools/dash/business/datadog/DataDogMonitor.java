package de.axelspringer.ideas.tools.dash.business.datadog;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class DataDogMonitor {

    public final static String STATE_OK = "OK";

    private String name;
    private String query;
    private String overall_state;
    private String type;

    public DataDogMonitor() {
    }

    public DataDogMonitor(String name, String overall_state) {
        this.name = name;
        this.overall_state = overall_state;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuery() {
        return this.query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getOverall_state() {
        return this.overall_state;
    }

    public void setOverall_state(String overall_state) {
        this.overall_state = overall_state;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
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
