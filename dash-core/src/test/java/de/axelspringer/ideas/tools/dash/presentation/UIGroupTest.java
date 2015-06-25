package de.axelspringer.ideas.tools.dash.presentation;

import de.axelspringer.ideas.tools.dash.business.check.CheckResult;
import de.axelspringer.ideas.tools.dash.business.failure.FailureGroup;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class UIGroupTest {

    @Test
    public void avoidDuplicatedCheckResults() throws Exception {
        final UIGroup uiGroup = new UIGroup();

        final CheckResult resultYellow1 = new CheckResult(State.YELLOW, "someName", "someInfo", 0, 0, FailureGroup.INSTANCE);
        final CheckResult resultGreen = new CheckResult(State.GREEN, "someName", "someInfo", 0, 0, FailureGroup.INSTANCE);
        final CheckResult resultYellow2 = new CheckResult(State.YELLOW, "someName", "someInfo", 0, 0, FailureGroup.INSTANCE);
        final CheckResult resultRed = new CheckResult(State.RED, "someName", "someInfo", 0, 0, FailureGroup.INSTANCE);

        uiGroup.add(resultYellow1);
        uiGroup.add(resultGreen);
        uiGroup.add(resultYellow2);
        uiGroup.add(resultRed);

        assertThat(uiGroup.getChecks().size(), is(3));
        assertThat(uiGroup.getChecks().get(0), is(resultYellow1));
        assertThat(uiGroup.getChecks().get(1), is(resultGreen));
        assertThat(uiGroup.getChecks().get(2), is(resultRed));
    }
}