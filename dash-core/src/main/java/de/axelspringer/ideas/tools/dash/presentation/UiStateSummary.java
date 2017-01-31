package de.axelspringer.ideas.tools.dash.presentation;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.function.BinaryOperator;


/**
 * Defines the current overall state using traffic light semantics. State will never be grey, but yellow instead.
 */
public class UiStateSummary {

    private State overallState;

    public UiStateSummary(State overallState) {
        this.overallState = overallState;
    }

    public State getOverallState() {
        return overallState;
    }

    public void setOverallState(State overallState) {
        this.overallState = overallState;
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

    public static UiStateSummary from(UiInfo infos) {
        final State res = infos.getGroups().stream()
            .map(UIGroup::getState).distinct()
            .map(state -> state == State.GREY ? State.YELLOW : state)
            .reduce(State.GREEN, BinaryOperator.maxBy(Enum::compareTo));
        return new UiStateSummary(res);
    }
}
