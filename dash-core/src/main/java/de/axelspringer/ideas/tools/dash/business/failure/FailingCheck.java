package de.axelspringer.ideas.tools.dash.business.failure;

import de.axelspringer.ideas.tools.dash.business.check.Check;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class FailingCheck extends Check {

    private final String failureMessage;

    public FailingCheck(String name, String failureMessage) {
        super(name, FailureGroup.INSTANCE, null);
        this.failureMessage = failureMessage;
    }

    public String getFailureMessage() {
        return this.failureMessage;
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
