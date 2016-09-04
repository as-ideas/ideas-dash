package de.axelspringer.ideas.tools.dash.example;

import de.axelspringer.ideas.tools.dash.business.customization.Group;

import java.util.Arrays;
import java.util.List;

public class ExampleGroups {

    public final static Group SIXTH = new Group(false, 6, null, "SIXTH");
    public final static Group FOURTH = new Group(false, 4, null, "FOURTH");
    public final static Group FIFTH = new Group(false, 5, null, "FIFTH");
    public final static Group FIRST = new Group(false, 1, null, "FIRST");
    public final static Group THIRD = new Group(false, 3, null, "THIRD");
    public final static Group SECOND = new Group(false, 2, null, "SECOND");

    public static List<Group> exampleGroups() {
        return Arrays.asList(SIXTH, FOURTH, FIFTH, FIRST, THIRD, SECOND);
    }
}
