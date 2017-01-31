package de.axelspringer.ideas.tools.dash.presentation;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.concurrent.atomic.AtomicReference;


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
        final AtomicReference<State> res = new AtomicReference<>(State.GREEN);
        infos.getGroups().forEach( uiGroup -> {
            final State stateToCompare = uiGroup.getState() == State.GREY ? State.YELLOW : uiGroup.getState();
            if(stateToCompare.compareTo(res.get()) > 0){
                res.set(stateToCompare);
            }
        });
        return new UiStateSummary(res.get());
    }
}
