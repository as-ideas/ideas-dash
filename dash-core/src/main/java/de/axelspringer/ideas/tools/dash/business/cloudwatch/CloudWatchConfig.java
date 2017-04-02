package de.axelspringer.ideas.tools.dash.business.cloudwatch;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudWatchConfig {

    @ConditionalOnMissingBean
    public CloudWatchStateMapper defaultCloudWatchStateMapper() {
        return new DefaultCloudWatchStateMapper();
    }
}
