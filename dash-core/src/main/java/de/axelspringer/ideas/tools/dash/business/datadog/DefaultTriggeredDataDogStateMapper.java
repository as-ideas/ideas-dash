package de.axelspringer.ideas.tools.dash.business.datadog;

import de.axelspringer.ideas.tools.dash.presentation.State;


public class DefaultTriggeredDataDogStateMapper implements TriggeredDataDogStateMapper {

    @Override
    public State map(DataDogMonitor monitor) {
        return State.RED;
    }
}
