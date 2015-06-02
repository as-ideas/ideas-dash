package de.axelspringer.ideas.tools.dash.business.failure;

import de.axelspringer.ideas.tools.dash.business.check.AbstractCheck;
import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.business.customization.Team;
import lombok.Data;

@Data
public class FailingCheck extends AbstractCheck {

    private final String failureMessage;

    public FailingCheck(String name, Group group, Team team, String failureMessage) {
        super(name, group, team);
        this.failureMessage = failureMessage;
    }
}
