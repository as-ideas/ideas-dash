package de.axelspringer.ideas.tools.dash.business.stash;

import de.axelspringer.ideas.tools.dash.presentation.State;
import lombok.Value;

public interface StateIndicator<T> {

    IndicateResult check(T t);

    @Value
    class IndicateResult {
        private final State state;
        private final String info;
    }
}
