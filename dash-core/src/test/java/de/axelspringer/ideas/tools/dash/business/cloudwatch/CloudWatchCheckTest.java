package de.axelspringer.ideas.tools.dash.business.cloudwatch;

import de.axelspringer.ideas.tools.dash.presentation.State;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by Tanemahuta on 27.03.17.
 */
@RunWith(Parameterized.class)
public class CloudWatchCheckTest {
    private final String sourceValue;
    private final State expectedState;

    public CloudWatchCheckTest(final String sourceValue, final State expectedState) {
        this.sourceValue = sourceValue;
        this.expectedState = expectedState;
    }

    @Test
    public void mapState() throws Exception {
        // expect:
        assertThat(CloudWatchCheck.mapState(sourceValue), equalTo(expectedState));
    }

    @Parameterized.Parameters(name = "{0} => {1}")
    public static Collection<Object[]> testParameters() {
        return Arrays.asList(
                new Object[]{"UNKNOWNldshfiuehsdiufh", State.GREY},
                new Object[]{null, State.GREY},
                new Object[]{"OK", State.GREEN},
                new Object[]{"INSUFFICIENT_DATA", State.YELLOW},
                new Object[]{"ALARM", State.RED}
        );
    }

}