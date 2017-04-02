package de.axelspringer.ideas.tools.dash.business.cloudwatch;

import de.axelspringer.ideas.tools.dash.presentation.State;

public class DefaultCloudWatchStateMapper implements CloudWatchStateMapper {
    @Override
    public State mapState(String stateValue) {
        if (stateValue == null) {
            return State.GREY;
        }
        switch (stateValue) {
            case "OK":
                return State.GREEN;
            case "INSUFFICIENT_DATA":
                return State.YELLOW;
            case "ALARM":
                return State.RED;
            default:
                return State.GREY;
        }
    }
}
