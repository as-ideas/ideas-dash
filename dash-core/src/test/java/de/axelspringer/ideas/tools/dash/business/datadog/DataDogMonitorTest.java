package de.axelspringer.ideas.tools.dash.business.datadog;

import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class DataDogMonitorTest {

    @Test
    public void findingTagsInQuery() throws Exception {
        final DataDogMonitor dataDogMonitor = monitorWithQuery("avg(last_1h):max:system.disk.in_use{pcp-jenkins} > 0.96 AND {something} OR {}");

        assertThat(dataDogMonitor.getTags().size(), is(2));
        assertThat(dataDogMonitor.getTags().contains("pcp-jenkins"), is(true));
        assertThat(dataDogMonitor.getTags().contains("something"), is(true));
        assertThat(dataDogMonitor.getTags().contains(""), is(false));
    }

    @Test
    public void findingTagsInQuery_NoTagsFound() throws Exception {
        final DataDogMonitor dataDogMonitor = monitorWithQuery("avg(last_1h):max:system.disk.in_use");

        assertThat(dataDogMonitor.getTags().size(), is(0));
        assertThat(dataDogMonitor.getTags().contains(""), is(false));
    }

    @Test
    public void findingTagsInQuery_WithMultipleTagsInOneValue() throws Exception {
        final DataDogMonitor dataDogMonitor = monitorWithQuery("avg(last_5m):avg:ipool.frontend_api.status_code{host:iideas-ipool01.asv.local,ipool_frontend_api} > 200");

        assertThat(dataDogMonitor.getTags().size(), is(2));
        assertThat(dataDogMonitor.getTags().contains("host:iideas-ipool01.asv.local"), is(true));
        assertThat(dataDogMonitor.getTags().contains("ipool_frontend_api"), is(true));
    }

    private DataDogMonitor monitorWithQuery(String query) {
        final DataDogMonitor dataDogMonitor = new DataDogMonitor();
        Whitebox.setInternalState(dataDogMonitor, "query", query);
        return dataDogMonitor;
    }
}