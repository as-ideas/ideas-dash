package de.axelspringer.ideas.tools.dash.example.config;

import de.axelspringer.ideas.tools.dash.business.customization.Group;

import java.util.Arrays;
import java.util.List;

public class ExampleGroups {

    public final static Group SIXTH = new Group(false, 6, "team-6", "SIXTH");
    public final static Group FOURTH = new Group(false, 4, "team-4", "FOURTH");
    public final static Group FIFTH = new Group(false, 5, "team-5", "FIFTH");
    public final static Group FIRST = new Group(false, 1, "team-1", "FIRST");
    public final static Group THIRD = new Group(false, 3, "team-3", "THIRD");
    public final static Group SECOND = new Group(false, 2, "team-2", "SECOND");

    public static List<Group> exampleGroups() {
        return Arrays.asList(SIXTH, FOURTH, FIFTH, FIRST, THIRD, SECOND);
    }
}
