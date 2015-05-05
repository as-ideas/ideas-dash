package de.axelspringer.ideas.tools.dash.business.datadog;

import com.google.gson.Gson;
import org.junit.Test;

import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class DataDogMonitorSerializationTest {

    private Gson gson = new Gson();

    @Test
    public void testDeserialization() {

        final InputStream testData = getClass().getResourceAsStream("/datadog-json-answer.json");
        assertNotNull(testData);
        final DataDogMonitor[] monitors = gson.fromJson(new InputStreamReader(testData), DataDogMonitor[].class);
        assertEquals(3, monitors.length);
    }
}