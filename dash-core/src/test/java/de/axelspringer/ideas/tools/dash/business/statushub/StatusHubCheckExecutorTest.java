package de.axelspringer.ideas.tools.dash.business.statushub;

import de.axelspringer.ideas.tools.dash.business.check.CheckResult;
import de.axelspringer.ideas.tools.dash.presentation.State;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StatusHubCheckExecutorTest {

    private final static String NO_WARNING_MARKUP = "<html>goo asdjkfhsdklhskldjcv <td headers=\"statuses in group YANA Status\">\n" +
            "<div class=\"single_status up  \" data-toggle=\"tooltip\" title=\"\" data-original-title=\"App\">\n" +
            "<em class=\"icon-warning-sign\"></em>\n" +
            "<em class=\"icon-wrench\"></em>\n" +
            "</div>\n" +
            "<div class=\"single_status up  \" data-toggle=\"tooltip\" title=\"\" data-original-title=\"App Backend\">\n" +
            "<em class=\"icon-warning-sign\"></em>\n" +
            "<em class=\"icon-wrench\"></em>\n" +
            "</div>\n" +
            "<div class=\"single_status up  \" data-toggle=\"tooltip\" title=\"\" data-original-title=\"Content Machine\">\n" +
            "<em class=\"icon-warning-sign\"></em>\n" +
            "<em class=\"icon-wrench\"></em>\n" +
            "</div>\n" +
            "<div class=\"single_status up  \" data-toggle=\"tooltip\" title=\"\" data-original-title=\"User event tracker\">\n" +
            "<em class=\"icon-warning-sign\"></em>\n" +
            "<em class=\"icon-wrench\"></em>\n" +
            "</div>\n" +
            "<div class=\"single_status up  \" data-toggle=\"tooltip\" title=\"\" data-original-title=\"Article Importer\">\n" +
            "<em class=\"icon-warning-sign\"></em>\n" +
            "<em class=\"icon-wrench\"></em>\n" +
            "</div>\n" +
            "<div class=\"single_status up  \" data-toggle=\"tooltip\" title=\"\" data-original-title=\"Recommender\">\n" +
            "<em class=\"icon-warning-sign\"></em>\n" +
            "<em class=\"icon-wrench\"></em>\n" +
            "</div>\n" +
            "<div class=\"single_status up  \" data-toggle=\"tooltip\" title=\"\" data-original-title=\"User event analyser\">\n" +
            "<em class=\"icon-warning-sign\"></em>\n" +
            "<em class=\"icon-wrench\"></em>\n" +
            "</div>\n" +
            "<div class=\"single_status up  \" data-toggle=\"tooltip\" title=\"\" data-original-title=\"Jenkin\">\n" +
            "<em class=\"icon-warning-sign\"></em>\n" +
            "<em class=\"icon-wrench\"></em>\n" +
            "</div>\n" +
            "<div class=\"single_status \" data-toggle=\"tooltip\" title=\"\" data-original-title=\"User event archiver\">\n" +
            "<em class=\"icon-warning-sign\"></em>\n" +
            "<em class=\"icon-wrench\"></em>\n" +
            "</div>\n" +
            "</td>";

    private final static String ONE_WARNING_MARKUP = "<html>goo asdjkfhsdklhskldjcv <td headers=\"statuses in group YANA Status\">\n" +
            "<div class=\"single_status up  \" data-toggle=\"tooltip\" title=\"\" data-original-title=\"App\">\n" +
            "<em class=\"icon-warning-sign\"></em>\n" +
            "<em class=\"icon-wrench\"></em>\n" +
            "</div>\n" +
            "<div class=\"single_status up  \" data-toggle=\"tooltip\" title=\"\" data-original-title=\"App Backend\">\n" +
            "<em class=\"icon-warning-sign\"></em>\n" +
            "<em class=\"icon-wrench\"></em>\n" +
            "</div>\n" +
            "<div class=\"single_status up  \" data-toggle=\"tooltip\" title=\"\" data-original-title=\"Content Machine\">\n" +
            "<em class=\"icon-warning-sign\"></em>\n" +
            "<em class=\"icon-wrench\"></em>\n" +
            "</div>\n" +
            "<div class=\"single_status up  \" data-toggle=\"tooltip\" title=\"\" data-original-title=\"User event tracker\">\n" +
            "<em class=\"icon-warning-sign\"></em>\n" +
            "<em class=\"icon-wrench\"></em>\n" +
            "</div>\n" +
            "<div class=\"single_status up  \" data-toggle=\"tooltip\" title=\"\" data-original-title=\"Article Importer\">\n" +
            "<em class=\"icon-warning-sign\"></em>\n" +
            "<em class=\"icon-wrench\"></em>\n" +
            "</div>\n" +
            "<div class=\"single_status up  \" data-toggle=\"tooltip\" title=\"\" data-original-title=\"Recommender\">\n" +
            "<em class=\"icon-warning-sign\"></em>\n" +
            "<em class=\"icon-wrench\"></em>\n" +
            "</div>\n" +
            "<div class=\"single_status up  \" data-toggle=\"tooltip\" title=\"\" data-original-title=\"User event analyser\">\n" +
            "<em class=\"icon-warning-sign\"></em>\n" +
            "<em class=\"icon-wrench\"></em>\n" +
            "</div>\n" +
            "<div class=\"single_status up  \" data-toggle=\"tooltip\" title=\"\" data-original-title=\"Jenkin\">\n" +
            "<em class=\"icon-warning-sign\"></em>\n" +
            "<em class=\"icon-wrench\"></em>\n" +
            "</div>\n" +
            "<div class=\"single_status degraded-performance incident \" data-toggle=\"tooltip\" title=\"\" data-original-title=\"User event archiver\">\n" +
            "<em class=\"icon-warning-sign\"></em>\n" +
            "<em class=\"icon-wrench\"></em>\n" +
            "</div>\n" +
            "</td>";

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private StatusHubCheckExecutor statusHubCheckExecutor;

    @Test
    public void testExecuteCheckNoIncidents() throws Exception {

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(NO_WARNING_MARKUP);
        final List<CheckResult> checkResults = statusHubCheckExecutor.executeCheck(statusHubCheck());
        assertEquals(1, checkResults.size());
        assertEquals(State.GREEN, checkResults.get(0).getState());
    }

    @Test
    public void testExecuteCheckOneIncident() throws Exception {

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(ONE_WARNING_MARKUP);
        final List<CheckResult> checkResults = statusHubCheckExecutor.executeCheck(statusHubCheck());
        assertEquals(1, checkResults.size());
        assertEquals(State.RED, checkResults.get(0).getState());
    }

    private StatusHubCheck statusHubCheck() {
        return new StatusHubCheck("foo", null, null, "bar");
    }
}