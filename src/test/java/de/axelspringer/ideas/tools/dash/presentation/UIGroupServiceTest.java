package de.axelspringer.ideas.tools.dash.presentation;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UIGroupServiceTest {

    private UIGroupsService uiGroupsService = new UIGroupsService();

    @Test
    public void testAggregate() {

        assertNull(uiGroupsService.aggregate(null, null));
        assertEquals(State.GREY, uiGroupsService.aggregate(null, State.GREY));
        assertEquals(State.GREY, uiGroupsService.aggregate(State.GREY, null));
        assertEquals(State.RED, uiGroupsService.aggregate(State.RED, State.YELLOW));
        assertEquals(State.YELLOW, uiGroupsService.aggregate(State.GREEN, State.YELLOW));
    }
}