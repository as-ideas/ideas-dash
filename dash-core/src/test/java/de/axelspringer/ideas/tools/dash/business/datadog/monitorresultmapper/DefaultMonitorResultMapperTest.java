package de.axelspringer.ideas.tools.dash.business.datadog.monitorresultmapper;

import de.axelspringer.ideas.tools.dash.business.check.CheckResult;
import de.axelspringer.ideas.tools.dash.business.datadog.DataDogCheck;
import de.axelspringer.ideas.tools.dash.business.datadog.DataDogDowntimes;
import de.axelspringer.ideas.tools.dash.business.datadog.DataDogMonitor;
import de.axelspringer.ideas.tools.dash.business.datadog.DataDogMonitorOptions;
import de.axelspringer.ideas.tools.dash.presentation.State;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;

public class DefaultMonitorResultMapperTest {

    private DefaultMonitorResultMapper resultMapper;

    @Before
    public void setUp() throws Exception {
        resultMapper = new DefaultMonitorResultMapper();
    }

    @Test
    public void mapToResult() {

        final CheckResult checkResult = resultMapper.mapToResult(dataDogMonitor("name", DataDogMonitor.STATE_OK), dataDogCheck(), downtimes());
        assertEquals(State.GREEN, checkResult.getState());
        assertEquals(1, checkResult.getTestCount());
        assertEquals(0, checkResult.getFailCount());
        assertEquals("name", checkResult.getName());
        assertEquals("OK", checkResult.getInfo());
        assertEquals("https://app.datadoghq.com/monitors#status?id=null&group=all", checkResult.getLink());
        assertNull(checkResult.getGroup());
        assertNull(checkResult.getTeam());
    }

    @Test
    public void mapToResult_Alert() {

        final CheckResult checkResult = resultMapper.mapToResult(dataDogMonitor("name", "alert"), dataDogCheck(), downtimes());
        assertEquals(State.RED, checkResult.getState());
        assertEquals(1, checkResult.getTestCount());
        assertEquals(1, checkResult.getFailCount());
        assertEquals("alert", checkResult.getInfo());
    }

    @Test
    public void mapToResult_Silenced() {

        final CheckResult checkResult = resultMapper.mapToResult(mute(dataDogMonitor("name", "alert")), dataDogCheck(), downtimes());
        assertEquals(State.GREEN, checkResult.getState());
        assertEquals(1, checkResult.getTestCount());
        assertEquals(0, checkResult.getFailCount());
        assertEquals("NOT ACTIVE (silenced)!", checkResult.getInfo());
    }

    @Test
    public void mapToResult_Downtime() {
        final DataDogDowntimes downtimes = downtimes();
        doReturn(true).when(downtimes).hasDowntime(any(DataDogMonitor.class));

        final CheckResult checkResult = resultMapper.mapToResult(dataDogMonitor("name", "alert"), dataDogCheck(), downtimes);
        assertEquals(State.GREEN, checkResult.getState());
        assertEquals(1, checkResult.getTestCount());
        assertEquals(0, checkResult.getFailCount());
        assertEquals("MAINTENANCE!", checkResult.getInfo());
    }

    @Test
    public void mapToResult_noData() throws Exception {

        DataDogMonitor monitor = dataDogMonitor("name", DataDogMonitor.STATE_NO_DATA);
        final CheckResult checkResult = resultMapper.mapToResult(monitor, dataDogCheck(), downtimes());
        assertEquals(State.GREEN, checkResult.getState());
        assertEquals(1, checkResult.getTestCount());
        assertEquals(0, checkResult.getFailCount());
        assertEquals("No Data", checkResult.getInfo());
    }

    @Test
    public void mapToResult_withNotifyNoData() throws Exception {

        DataDogMonitor monitor = enableNotifyNoData(dataDogMonitor("name", DataDogMonitor.STATE_NO_DATA));
        final CheckResult checkResult = resultMapper.mapToResult(monitor, dataDogCheck(), downtimes());

        assertEquals(State.RED, checkResult.getState());
        assertEquals(1, checkResult.getTestCount());
        assertEquals(1, checkResult.getFailCount());
        assertEquals("No Data", checkResult.getInfo());
    }

    private DataDogMonitor dataDogMonitor(String name, String stateOk) {
        DataDogMonitor monitor = new DataDogMonitor();
        Whitebox.setInternalState(monitor, "name", name);
        Whitebox.setInternalState(monitor, "overallState", stateOk);
        return monitor;
    }

    private DataDogCheck dataDogCheck() {
        return new DataDogCheck("name", null, "apiKey", "appKey", "nameFilter");
    }

    private DataDogDowntimes downtimes() {
        return Mockito.mock(DataDogDowntimes.class);
    }

    private DataDogMonitor mute(DataDogMonitor monitor) {
        DataDogMonitorOptions options = new DataDogMonitorOptions();
        Map<String, Object> silenced = new HashMap<>();
        silenced.put("*", null);
        Whitebox.setInternalState(options, "silenced", silenced);
        Whitebox.setInternalState(monitor, "options", options);
        return monitor;
    }

    private DataDogMonitor enableNotifyNoData(DataDogMonitor monitor) {
        DataDogMonitorOptions options = new DataDogMonitorOptions();
        Whitebox.setInternalState(options, "notifyNoData", true);
        Whitebox.setInternalState(monitor, "options", options);
        return monitor;
    }
}