package de.axelspringer.ideas.tools.dash.business.datadog;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpMessageConverterExtractor;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;

public class DataDogMonitorTest {

    @Test
    public void deserialisation() throws Exception {
        String jsonValue = "{\n" +
                "\"creator\": {\n" +
                "\"id\": 30921,\n" +
                "\"handle\": \"goekhan.makinist@axelspringer.de\",\n" +
                "\"email\": \"goekhan.makinist@axelspringer.de\",\n" +
                "\"name\": \"Goekhan Makinist\"\n" +
                "},\n" +
                "\"query\": \"sum(last_10m):sum:pcp_errors_total{*} > 800\",\n" +
                "\"message\": \"@webhook-Slack-pcp_alerts @servicenow\",\n" +
                "\"id\": 123329,\n" +
                "\"multi\": false,\n" +
                "\"name\": \"[PCP] Total_errors_above_800_in_last_10_min\",\n" +
                "\"created_at\": 1421688664000,\n" +
                "\"org_id\": 7561,\n" +
                "\"overall_state\": \"No Data\",\n" +
                "\"type\": \"metric alert\",\n" +
                "\"options\": {\n" +
                "\"notify_audit\": true,\n" +
                "\"timeout_h\": 0,\n" +
                "\"silenced\": {\n" +
                "\"*\": null\n" +
                "},\n" +
                "\"no_data_timeframe\": 20,\n" +
                "\"notify_no_data\": true,\n" +
                "\"renotify_interval\": 60\n" +
                "}\n" +
                "}";

        ClientHttpResponse response = Mockito.mock(ClientHttpResponse.class);
        doReturn(new ByteArrayInputStream(jsonValue.getBytes(StandardCharsets.UTF_8))).when(response).getBody();
        doReturn(HttpStatus.OK).when(response).getStatusCode();
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        doReturn(headers).when(response).getHeaders();

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        messageConverters.add(new MappingJackson2HttpMessageConverter());

        final HttpMessageConverterExtractor<DataDogMonitor> messageConverterExtractor = new HttpMessageConverterExtractor<>(DataDogMonitor.class, messageConverters);
        final DataDogMonitor dataDogMonitor = messageConverterExtractor.extractData(response);

        assertNotNull(dataDogMonitor.getOptions());
        assertNotNull(dataDogMonitor.getOverallState());
        assertTrue(dataDogMonitor.isSilencedMonitor());
        assertTrue(dataDogMonitor.isNotifyNoData());
    }


    @Test
    public void findingTagsInQuery() throws Exception {
        final DataDogMonitor dataDogMonitor = monitorWithQuery("avg(last_1h):max:system.disk.in_use{pcp-jenkins} > 0.96 AND {something} OR {}");

        assertThat(dataDogMonitor.getTags().size(), is(2));
        assertThat(dataDogMonitor.getTags().contains("pcp-jenkins"), is(true));
        assertThat(dataDogMonitor.getTags().contains("something"), is(true));
        assertThat(dataDogMonitor.getTags().contains(""), is(false));
    }

    @Test
    public void findingTagsInQuery2() throws Exception {
        final DataDogMonitor dataDogMonitor = monitorWithQuery("max(last_10m):min:app_logs.count{host:pcp-prod-vm7.ocb-as.boreus.de,app_logs.app_name:tvconnect,pcp-vm,pcp-prod} < 1");

        assertThat(dataDogMonitor.getTags().size(), is(4));
        assertThat(dataDogMonitor.getTags().contains("host:pcp-prod-vm7.ocb-as.boreus.de"), is(true));
        assertThat(dataDogMonitor.getTags().contains("app_logs.app_name:tvconnect"), is(true));
        assertThat(dataDogMonitor.getTags().contains("pcp-vm"), is(true));
        assertThat(dataDogMonitor.getTags().contains("pcp-prod"), is(true));
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