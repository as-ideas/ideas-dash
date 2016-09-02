package de.axelspringer.ideas.tools.dash.business.failure;

import de.axelspringer.ideas.tools.dash.business.customization.AbstractGroup;

public class FailureGroup extends AbstractGroup {

    public static final FailureGroup INSTANCE = new FailureGroup();

    @Override
    public String getGroupId() {
        return "ERRORS";
    }

    @Override
    public String getJiraName() {
        return null;
    }

    @Override
    public int getOrderScore() {
        return -1;
    }

    @Override
    public String toString() {
        return getGroupId();
    }
}
