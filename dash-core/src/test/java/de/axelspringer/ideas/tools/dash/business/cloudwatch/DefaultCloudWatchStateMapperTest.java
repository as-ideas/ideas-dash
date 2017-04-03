package de.axelspringer.ideas.tools.dash.business.cloudwatch;

import de.axelspringer.ideas.tools.dash.presentation.State;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class DefaultCloudWatchStateMapperTest {

    private DefaultCloudWatchStateMapper stateMapper = new DefaultCloudWatchStateMapper();

    private final String sourceValue;
    private final State expectedState;

    public DefaultCloudWatchStateMapperTest(final String sourceValue, final State expectedState) {
        this.sourceValue = sourceValue;
        this.expectedState = expectedState;
    }

    @Test
    public void mapState() throws Exception {
        assertThat(stateMapper.mapState(sourceValue), equalTo(expectedState));
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