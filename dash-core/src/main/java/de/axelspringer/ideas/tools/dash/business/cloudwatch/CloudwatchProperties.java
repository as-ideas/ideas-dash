package de.axelspringer.ideas.tools.dash.business.cloudwatch;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "dash.cloudwatch")
public class CloudwatchProperties {

    private Map<String, List<String>> alarmToTeamsMapping;

    public Map<String, List<String>> getAlarmToTeamsMapping() {
        return alarmToTeamsMapping;
    }

    public void setAlarmToTeamsMapping(Map<String, List<String>> alarmToTeamsMapping) {
        this.alarmToTeamsMapping = alarmToTeamsMapping;
    }
}
