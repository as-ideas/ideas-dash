package de.axelspringer.ideas.tools.dash.business.datadog;

import de.axelspringer.ideas.tools.dash.TestTeam;
import de.axelspringer.ideas.tools.dash.business.check.CheckResult;
import de.axelspringer.ideas.tools.dash.business.customization.Team;
import de.axelspringer.ideas.tools.dash.presentation.State;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DataDogCheckExecutorTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private DataDogCheckExecutor dataDogCheckExecutor;

    @Before
    public void beforeMethod() throws Exception {
        doReturn(new ResponseEntity<>(HttpStatus.CONFLICT)).when(restTemplate).getForEntity(anyString(), anyObject());
    }

    @Test
    public void executeCheck_AllResultsOk() throws Exception {

        // simulate successful backend call
        mockDatadogApiCallAndReturn(dataDogMonitor("Monitor OK", DataDogMonitor.STATE_OK), dataDogMonitor("Monitor in Error", "some_error_state"));

        // execute check
        final List<CheckResult> checkResults = dataDogCheckExecutor.executeCheck(new DataDogCheck("myCheck", null, null, null, null));

        assertEquals(2, checkResults.size());

        final List<CheckResult> red = checkResults.stream().filter((checkResult) -> State.RED == checkResult.getState()).collect(Collectors.toList());
        assertEquals(1, red.size());
        assertEquals("Monitor in Error@DataDog", red.get(0).getName());
        assertEquals("some_error_state (query: null)", red.get(0).getInfo());

        final List<CheckResult> green = checkResults.stream().filter((checkResult) -> State.GREEN == checkResult.getState()).collect(Collectors.toList());
        assertEquals(1, green.size());
        assertEquals("Monitor OK@DataDog", green.get(0).getName());
        assertEquals("OK (query: null)", green.get(0).getInfo());
    }

    @Test
    public void executeCheck_noDataMonitor() throws Exception {

        // simulate successful backend call
        mockDatadogApiCallAndReturn(dataDogMonitor("Monitor OK", DataDogMonitor.STATE_NO_DATA));

        // execute check
        final List<CheckResult> checkResults = dataDogCheckExecutor.executeCheck(new DataDogCheck("myCheck", null, null, null, null));

        assertEquals(1, checkResults.size());
        assertEquals(State.GREEN, checkResults.get(0).getState());
        assertEquals("Monitor OK@DataDog", checkResults.get(0).getName());
        assertEquals("No Data (query: null)", checkResults.get(0).getInfo());
    }

    @Test
    public void executeCheck_noDataMonitor_withNotifyNoData() throws Exception {

        // simulate successful backend call
        mockDatadogApiCallAndReturn(enableNotifyNoData(dataDogMonitor("Monitor not OK", DataDogMonitor.STATE_NO_DATA)));

        // execute check
        final List<CheckResult> checkResults = dataDogCheckExecutor.executeCheck(new DataDogCheck("myCheck", null, null, null, null));

        assertEquals(1, checkResults.size());
        assertEquals(State.RED, checkResults.get(0).getState());
        assertEquals("Monitor not OK@DataDog", checkResults.get(0).getName());
        assertEquals("No Data (query: null)", checkResults.get(0).getInfo());
    }

    @Test
    public void executeCheck_WithNameFiler() throws Exception {

        // simulate successful backend call
        mockDatadogApiCallAndReturn(dataDogMonitor("[YANA]Monitor OK", DataDogMonitor.STATE_OK), dataDogMonitor("Monitor in Error", "some_error_state"));

        // execute check with name filter (should work case-insensitive)
        final List<CheckResult> checkResults = dataDogCheckExecutor.executeCheck(new DataDogCheck("myCheck", null, null, null, "[yana]"));

        assertEquals(1, checkResults.size());

        assertEquals("[YANA]Monitor OK@DataDog", checkResults.get(0).getName());
        assertEquals("OK (query: null)", checkResults.get(0).getInfo());
    }

    @Test
    public void executeCheck_WithHttpError() throws Exception {

        // simulate some code other than 200
        mockDatadogApiCallAndReturn(HttpStatus.BAD_GATEWAY);

        // execute check
        final List<CheckResult> checkResults = dataDogCheckExecutor.executeCheck(mock(DataDogCheck.class));

        assertEquals(1, checkResults.size());

        final CheckResult checkResult = checkResults.get(0);

        assertEquals(State.RED, checkResult.getState());
        assertEquals("DataDog", checkResult.getName());
        assertEquals("got http 502", checkResult.getInfo());
    }

    @Test
    public void convertMonitorToCheckResult() {

        final CheckResult checkResult = dataDogCheckExecutor.convertMonitorToCheckResult(dataDogMonitor("name", DataDogMonitor.STATE_OK), dataDogCheck(), downtimes());
        assertEquals(State.GREEN, checkResult.getState());
        assertEquals(1, checkResult.getTestCount());
        assertEquals(0, checkResult.getFailCount());
        assertEquals("name@DataDog", checkResult.getName());
        assertEquals("OK (query: null)", checkResult.getInfo());
        assertEquals("https://app.datadoghq.com/monitors#status?id=null&group=all", checkResult.getLink());
        assertNull(checkResult.getGroup());
        assertNull(checkResult.getTeam());
    }

    @Test
    public void convertMonitorToCheckResult_Alert() {

        final CheckResult checkResult = dataDogCheckExecutor.convertMonitorToCheckResult(dataDogMonitor("name", "alert"), dataDogCheck(), downtimes());
        assertEquals(State.RED, checkResult.getState());
        assertEquals(1, checkResult.getTestCount());
        assertEquals(1, checkResult.getFailCount());
        assertEquals("name@DataDog", checkResult.getName());
        assertEquals("alert (query: null)", checkResult.getInfo());
        assertEquals("https://app.datadoghq.com/monitors#status?id=null&group=all", checkResult.getLink());
        assertNull(checkResult.getGroup());
        assertNull(checkResult.getTeam());
    }

    @Test
    public void convertMonitorToCheckResult_Silenced() {

        final CheckResult checkResult = dataDogCheckExecutor.convertMonitorToCheckResult(dataDogMonitor("name", "alert"), dataDogCheck(), downtimes());
        assertEquals(State.RED, checkResult.getState());
        assertEquals(1, checkResult.getTestCount());
        assertEquals(1, checkResult.getFailCount());
        assertEquals("name@DataDog", checkResult.getName());
        assertEquals("alert (query: null)", checkResult.getInfo());
        assertEquals("https://app.datadoghq.com/monitors#status?id=null&group=all", checkResult.getLink());
        assertNull(checkResult.getGroup());
        assertNull(checkResult.getTeam());
    }

    @Test
    public void convertMonitorToCheckResult_Downtime() {
        final DataDogDowntimes downtimes = downtimes();
        doReturn(true).when(downtimes).hasDowntime(any(DataDogMonitor.class));

        final CheckResult checkResult = dataDogCheckExecutor.convertMonitorToCheckResult(dataDogMonitor("name", "alert"), dataDogCheck(), downtimes);
        assertEquals(State.GREEN, checkResult.getState());
        assertEquals(1, checkResult.getTestCount());
        assertEquals(1, checkResult.getFailCount());
        assertEquals("name@DataDog", checkResult.getName());
        assertEquals("MAINTENANCE!", checkResult.getInfo());
        assertEquals("https://app.datadoghq.com/monitors#status?id=null&group=all", checkResult.getLink());
        assertNull(checkResult.getGroup());
        assertNull(checkResult.getTeam());
    }

    @Test
    public void decideTeam() {

        assertNull(dataDogCheckExecutor.decideTeam("[yana][cm]some_monitor", null, teamMappings()));
        assertNull(dataDogCheckExecutor.decideTeam("[yana][foo]some_monitor", "[yana]", teamMappings()));
        assertNull(dataDogCheckExecutor.decideTeam("some_monitor", null, teamMappings()));
        assertEquals(TestTeam.INSTANCE, dataDogCheckExecutor.decideTeam("[yana][cm]some_monitor", "[yana]", teamMappings()));
    }

    private DataDogDowntimes downtimes() {
        return Mockito.mock(DataDogDowntimes.class);
    }

    private DataDogCheck dataDogCheck() {
        return new DataDogCheck("name", null, "apiKey", "appKey", "nameFilter");
    }

    private DataDogMonitor dataDogMonitor(String name, String stateOk) {
        DataDogMonitor monitor = new DataDogMonitor();
        Whitebox.setInternalState(monitor, "name", name);
        Whitebox.setInternalState(monitor, "overallState", stateOk);
        return monitor;
    }

    private DataDogMonitor enableNotifyNoData(DataDogMonitor monitor) {
        DataDogMonitorOptions options = new DataDogMonitorOptions();
        Whitebox.setInternalState(options, "notifyNoData", true);
        Whitebox.setInternalState(monitor, "options", options);
        return monitor;
    }

    private Map<String, Team> teamMappings() {

        Map<String, Team> teamMappings = new HashMap<>();
        teamMappings.put("[cm]", TestTeam.INSTANCE);
        return teamMappings;
    }

    private void mockDatadogApiCallAndReturn(DataDogMonitor... monitors) {
        when(restTemplate.getForEntity(anyString(), eq(DataDogMonitor[].class))).thenReturn(new ResponseEntity<>(monitors, HttpStatus.OK));
    }

    private void mockDatadogApiCallAndReturn(HttpStatus httpStatus) {
        when(restTemplate.getForEntity(anyString(), eq(DataDogMonitor[].class))).thenReturn(new ResponseEntity<>(httpStatus));
    }
}