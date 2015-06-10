package de.axelspringer.ideas.tools.dash.business.fabric;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * DTO for fabric config endpoint
 */
public class FabricConfig {

    private String developer_token;

    public FabricConfig() {
    }

    public String getDeveloper_token() {
        return this.developer_token;
    }

    public void setDeveloper_token(String developer_token) {
        this.developer_token = developer_token;
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
