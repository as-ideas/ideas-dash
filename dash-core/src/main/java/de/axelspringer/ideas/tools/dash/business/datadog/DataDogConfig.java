package de.axelspringer.ideas.tools.dash.business.datadog;

import de.axelspringer.ideas.tools.dash.business.datadog.monitorresultmapper.DefaultMonitorResultMapper;
import de.axelspringer.ideas.tools.dash.business.datadog.monitorresultmapper.MonitorResultMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataDogConfig {

    @ConditionalOnMissingBean
    @Bean
    public MonitorResultMapper monitorStateMapper() {
        return new DefaultMonitorResultMapper();
    }
}
