package de.axelspringer.ideas.tools.dash.example;

import de.axelspringer.ideas.tools.dash.business.customization.Group;

public enum ExampleGroup implements Group {

    EXPECTED_SIXTH("Sixth", 6),
    EXPECTED_FOURTH("Fourth", 4),
    EXPECTED_FIFTH("Fifth", 5),
    EXPECTED_FIRST("First", 1),
    EXPECTED_THIRD("Third", 3),
    EXPECTED_SECOND("Second", 2);

    private String groupId;

    private Integer orderScore;

    private ExampleGroup(String groupId, Integer orderScore) {
        this.groupId = groupId;
        this.orderScore = orderScore;
    }

    @Override
    public String getGroupId() {
        return groupId;
    }

    @Override
    public int getOrderScore() {
        return orderScore;
    }

    @Override
    public String getJiraName() {
        return null;
    }

    @Override
    public String toString() {
        return getGroupId();
    }
}
