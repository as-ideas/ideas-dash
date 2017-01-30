package de.axelspringer.ideas.tools.dash.presentation;

import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class UiStateSummaryTest {

    @Test
    public void from__Should_Return_Most_Urgent_State() throws Exception {
        UiInfo givenUiInfo = givenUiInfoWith_greenYellowRedState();
        final UiStateSummary res = UiStateSummary.from(givenUiInfo);

        assertThat(res.getOverallState(), is(State.RED));
    }

    @Test
    public void from__Should_Return_Yellow_On_Only_Gray() throws Exception {
        UiInfo givenUiInfo = givenUiInfoWith_grayState();
        final UiStateSummary res = UiStateSummary.from(givenUiInfo);

        assertThat(res.getOverallState(), is(State.YELLOW));
    }

    @Test
    public void from__Should_Return_Yellow_OnGreen_And_Yellow() throws Exception {
        UiInfo givenUiInfo = givenUiInfoWith_greenAndYellowState();
        final UiStateSummary res = UiStateSummary.from(givenUiInfo);

        assertThat(res.getOverallState(), is(State.YELLOW));
    }

    @Test
    public void from__Should_Return_Green_OnGreensOnly() throws Exception {
        UiInfo givenUiInfo = givenUiInfoWith_greenStates();
        final UiStateSummary res = UiStateSummary.from(givenUiInfo);

        assertThat(res.getOverallState(), is(State.GREEN));
    }

    private UiInfo givenUiInfoWith_greenStates() {
        final UiInfo res = new UiInfo("anId");

        final UIGroup uiGroup_green = new UIGroup();
        uiGroup_green.setState(State.GREEN);
        final UIGroup uiGroup_green_2 = new UIGroup();
        uiGroup_green_2.setState(State.GREEN);

        res.add(uiGroup_green);
        res.add(uiGroup_green_2);

        return res;
    }

    private UiInfo givenUiInfoWith_greenAndYellowState() {
        final UiInfo res = new UiInfo("anId");

        final UIGroup uiGroup_green = new UIGroup();
        uiGroup_green.setState(State.GREY);
        final UIGroup uiGroup_yellow = new UIGroup();
        uiGroup_yellow.setState(State.YELLOW);

        res.add(uiGroup_green);
        res.add(uiGroup_yellow);

        return res;
    }

    private UiInfo givenUiInfoWith_grayState() {
        final UiInfo res = new UiInfo("anId");

        final UIGroup uiGroup_gray = new UIGroup();
        uiGroup_gray.setState(State.GREY);

        res.add(uiGroup_gray);

        return res;
    }

    private UiInfo givenUiInfoWith_greenYellowRedState() {
        final UiInfo res = new UiInfo("anId");

        final UIGroup uiGroup_green = new UIGroup();
        uiGroup_green.setState(State.GREEN);
        final UIGroup uiGroup_grey = new UIGroup();
        uiGroup_grey.setState(State.GREY);
        final UIGroup uiGroup_yellow = new UIGroup();
        uiGroup_yellow.setState(State.YELLOW);
        final UIGroup uiGroup_red = new UIGroup();
        uiGroup_red.setState(State.RED);

        res.add(uiGroup_green);
        res.add(uiGroup_grey);
        res.add(uiGroup_yellow);
        res.add(uiGroup_red);

        return res;
    }

}