package de.axelspringer.ideas.tools.dash.business.datadog;

import de.axelspringer.ideas.tools.dash.business.check.CheckResult;
import de.axelspringer.ideas.tools.dash.business.datadog.monitorresultmapper.DefaultMonitorResultMapper;
import de.axelspringer.ideas.tools.dash.config.ClientConfig;
import de.axelspringer.ideas.tools.dash.presentation.State;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(MockitoJUnitRunner.class)
public class DataDogCheckExecutorTest {

    private static final CheckResult result = new CheckResult(State.GREEN, "name", "info", 1, 0, null);;

    @Mock
    private DefaultMonitorResultMapper monitorResultMapper;

    @InjectMocks
    private DataDogCheckExecutor dataDogCheckExecutor;

    private MockRestServiceServer mockServer;

    @Before
    public void beforeMethod() throws Exception {
        RestTemplate restTemplate = new ClientConfig().exceptionSwallowingRestTemplate();
        Whitebox.setInternalState(dataDogCheckExecutor, "restTemplate", restTemplate);
        mockServer = MockRestServiceServer.createServer(restTemplate);

        when(monitorResultMapper.mapToResult(any(DataDogMonitor.class), any(DataDogCheck.class), any(DataDogDowntimes.class))).thenReturn(result);
    }

    @Test
    public void executeCheck_AllResultsOk() throws Exception {

        mockServer.expect(requestTo("https://app.datadoghq.com/api/v1/monitor?api_key=apikey&application_key=appkey"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("[{},{}]", MediaType.APPLICATION_JSON));

        mockDowntimesRestCall();

        // execute check
        final List<CheckResult> checkResults = dataDogCheckExecutor.executeCheck(check(null));

        mockServer.verify();

        assertEquals(2, checkResults.size());
        assertEquals(result, checkResults.get(0));
        assertEquals(result, checkResults.get(1));
    }

    @Test
    public void executeCheck_WithNameFiler() throws Exception {

        // simulate successful backend call
        mockServer.expect(requestTo("https://app.datadoghq.com/api/v1/monitor?api_key=apikey&application_key=appkey"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("[{\"name\":\"[YANA]Monitor 1\"},{\"name\":\"Monitor 2\"}]", MediaType.APPLICATION_JSON));

        mockDowntimesRestCall();

        // execute check with name filter (should work case-insensitive)
        final List<CheckResult> checkResults = dataDogCheckExecutor.executeCheck(check("[yana]"));

        assertEquals(1, checkResults.size());
        assertEquals(result, checkResults.get(0));
    }

    @Test
    public void executeCheck_WithHttpError() throws Exception {

        // simulate some code other than 200
        mockServer.expect(requestTo("https://app.datadoghq.com/api/v1/monitor?api_key=apikey&application_key=appkey"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withBadRequest());

        mockDowntimesRestCall();

        // execute check
        final List<CheckResult> checkResults = dataDogCheckExecutor.executeCheck(check(null));

        assertEquals(1, checkResults.size());

        final CheckResult checkResult = checkResults.get(0);

        assertEquals(State.RED, checkResult.getState());
        assertEquals("DataDog", checkResult.getName());
        assertEquals("got http 400", checkResult.getInfo());
    }

    private DataDogCheck check(String nameFilter) {
        return new DataDogCheck("myCheck", null, "apikey", "appkey", nameFilter);
    }

    private void mockDowntimesRestCall() {
        mockServer.expect(requestTo("https://app.datadoghq.com/api/v1/downtime?current_only=true&api_key=apikey&application_key=appkey"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("[]", MediaType.APPLICATION_JSON));
    }
}