package de.axelspringer.ideas.tools.dash.business.cloudwatch;

import de.axelspringer.ideas.tools.dash.presentation.State;

public interface CloudWatchStateMapper {

    State mapState(final String stateValue);
}
