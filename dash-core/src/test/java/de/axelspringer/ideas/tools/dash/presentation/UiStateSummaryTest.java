package de.axelspringer.ideas.tools.dash.presentation;

import org.junit.Test;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Collection;

import static de.axelspringer.ideas.tools.dash.presentation.State.GREEN;
import static de.axelspringer.ideas.tools.dash.presentation.State.GREY;
import static de.axelspringer.ideas.tools.dash.presentation.State.RED;
import static de.axelspringer.ideas.tools.dash.presentation.State.YELLOW;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class UiStateSummaryTest {

    @Test
    public void from__Should_Return_Most_Urgent_State() throws Exception {
        UiInfo givenUiInfo = givenUiInfoWithStates(Arrays.asList(GREEN, GREY, YELLOW, RED));
        final UiStateSummary res = UiStateSummary.from(givenUiInfo);

        assertThat(res.getOverallState(), is(RED));
    }

    @Test
    public void from__Should_Return_Yellow_On_Only_Gray() throws Exception {
        UiInfo givenUiInfo = givenUiInfoWithStates(Arrays.asList(GREY));
        final UiStateSummary res = UiStateSummary.from(givenUiInfo);

        assertThat(res.getOverallState(), is(YELLOW));
    }

    @Test
    public void from__Should_Return_Yellow_OnGreen_And_Yellow() throws Exception {
        UiInfo givenUiInfo = givenUiInfoWithStates(Arrays.asList(GREEN, YELLOW));
        final UiStateSummary res = UiStateSummary.from(givenUiInfo);

        assertThat(res.getOverallState(), is(YELLOW));
    }

    @Test
    public void from__Should_Return_Green_OnGreensOnly() throws Exception {
        UiInfo givenUiInfo = givenUiInfoWithStates(Arrays.asList(GREEN, GREEN));
        final UiStateSummary res = UiStateSummary.from(givenUiInfo);

        assertThat(res.getOverallState(), is(GREEN));
    }

    private UiInfo givenUiInfoWithStates(Collection<State> states) {
        Assert.notNull(states);

        final UiInfo res = new UiInfo("anId");

        UIGroup uiGroup;
        for (State el : states) {
            uiGroup = new UIGroup();
            uiGroup.setState(el);
            res.add(uiGroup);
        }
        return res;
    }

}