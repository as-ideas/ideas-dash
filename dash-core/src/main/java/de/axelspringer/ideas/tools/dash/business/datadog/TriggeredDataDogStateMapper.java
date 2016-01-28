package de.axelspringer.ideas.tools.dash.business.datadog;

import de.axelspringer.ideas.tools.dash.presentation.State;

/**
 * Maps a triggered datadog monitor to a state. This way it is possible to customize the color the monitor will be
 * displayed on the UI. For example: A triggered monitor whose name contains OCD is displayed in red, others in
 * yellow. Like this you can express the severity of the monitor. Monitors that are not triggered will always be
 * displayed in {@link State#GREEN}, independently of the implementation of this mapper.
 */
public interface TriggeredDataDogStateMapper {

    State map(DataDogMonitor monitor);
}
