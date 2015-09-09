package de.axelspringer.ideas.tools.dash.business.datadog;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;

public class DataDogDowntimesTest {

    private DataDogDowntimes dataDogDowntimes;

    private RestTemplate restTemplate;

    @Before
    public void beforeMethod() throws Exception {
        restTemplate = Mockito.mock(RestTemplate.class);
    }

    @Test
    public void loadingData_conflict() throws Exception {
        doReturn(badResponse()).when(restTemplate).getForEntity(anyString(), anyObject());

        dataDogDowntimes = new DataDogDowntimes(restTemplate, "", "");

        assertThat(dataDogDowntimes.getDowntimes().size(), is(0));
    }

    @Test
    public void loadingData_ok() throws Exception {
        doReturn(okResponse()).when(restTemplate).getForEntity(anyString(), anyObject());

        dataDogDowntimes = new DataDogDowntimes(restTemplate, "", "");

        assertThat(dataDogDowntimes.getDowntimes().size(), is(1));
        assertThat(dataDogDowntimes.hasDowntime(monitor("pcp-jenkins")), is(true));
    }

    @Test
    public void hasDowntime() throws Exception {
        givenDowntimes(downtime("a", "b"), downtime("a", "c"));

        dataDogDowntimes = new DataDogDowntimes(restTemplate, "", "");

        assertThat(dataDogDowntimes.hasDowntime(monitor("a")), is(false));
        assertThat(dataDogDowntimes.hasDowntime(monitor("a", "b")), is(true));
        assertThat(dataDogDowntimes.hasDowntime(monitor("a", "b", "c")), is(true));
        assertThat(dataDogDowntimes.hasDowntime(monitor("a", "c")), is(true));
    }

    private DataDogDowntime downtime(String... downtimes) {
        final DataDogDowntime dataDogDowntime = new DataDogDowntime();
        dataDogDowntime.scope = Arrays.asList(downtimes);
        return dataDogDowntime;
    }

    private DataDogMonitor monitor(String... tags) {
        final DataDogMonitor dataDogMonitor = Mockito.mock(DataDogMonitor.class, Mockito.CALLS_REAL_METHODS);
        doReturn(Arrays.asList(tags)).when(dataDogMonitor).getTags();
        return dataDogMonitor;
    }

    private ResponseEntity<DataDogDowntime[]> badResponse() {
        final DataDogDowntime dataDogDowntime = new DataDogDowntime();

        DataDogDowntime[] downtimes = new DataDogDowntime[]{dataDogDowntime};
        return new ResponseEntity<>(downtimes, HttpStatus.CONFLICT);
    }


    private ResponseEntity<DataDogDowntime[]> okResponse() {
        final DataDogDowntime dataDogDowntime = new DataDogDowntime();
        dataDogDowntime.scope = Arrays.asList("pcp-jenkins");

        DataDogDowntime[] downtimes = new DataDogDowntime[]{dataDogDowntime};
        return new ResponseEntity<>(downtimes, HttpStatus.OK);
    }

    private void givenDowntimes(DataDogDowntime... dataDogDowntimes) {
        doReturn(new ResponseEntity<>(dataDogDowntimes, HttpStatus.OK)).when(restTemplate).getForEntity(anyString(), anyObject());

    }
}