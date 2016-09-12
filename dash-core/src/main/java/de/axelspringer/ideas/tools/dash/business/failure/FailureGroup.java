package de.axelspringer.ideas.tools.dash.business.failure;

import de.axelspringer.ideas.tools.dash.business.customization.Group;

public class FailureGroup extends Group {

    public static final FailureGroup INSTANCE = new FailureGroup();

    public FailureGroup() {
        super(false, -1, null, "ERRORS");
    }

    @Override
    public String toString() {
        return getGroupId();
    }
}
