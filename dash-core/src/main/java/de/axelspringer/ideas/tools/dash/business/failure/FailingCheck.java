package de.axelspringer.ideas.tools.dash.business.failure;

import de.axelspringer.ideas.tools.dash.business.check.AbstractCheck;
import lombok.Data;

@Data
public class FailingCheck extends AbstractCheck {

    private final String failureMessage;

    public FailingCheck(String name, String failureMessage) {
        super(name, FailureGroup.INSTANCE, null);
        this.failureMessage = failureMessage;
    }
}
