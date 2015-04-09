package de.axelspringer.ideas.tools.dash.presentation;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UIGroupServiceTest {

    private UiInfoService uiInfoService = new UiInfoService();

    @Test
    public void testAggregate() {

        assertNull(uiInfoService.aggregate(null, null));
        assertEquals(State.GREY, uiInfoService.aggregate(null, State.GREY));
        assertEquals(State.GREY, uiInfoService.aggregate(State.GREY, null));
        assertEquals(State.RED, uiInfoService.aggregate(State.RED, State.YELLOW));
        assertEquals(State.YELLOW, uiInfoService.aggregate(State.GREEN, State.YELLOW));
    }
}