package de.axelspringer.ideas.tools.dash.business.check;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Host {

    private String nameIp;

    public Host(String nameIp) {
        this.nameIp = nameIp;
    }

    public String getNameIp() {
        return this.nameIp;
    }

    public void setNameIp(String nameIp) {
        this.nameIp = nameIp;
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
