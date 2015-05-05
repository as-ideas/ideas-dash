package de.axelspringer.ideas.tools.dash;

import com.google.gson.Gson;
import de.axelspringer.ideas.tools.dash.config.ClientConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ComponentScan(basePackages = "de.axelspringer.ideas.tools.dash", excludeFilters = @ComponentScan.Filter(Configuration.class))
@Import(ClientConfig.class)
public class DashConfig {

    @Bean
    public Gson gson() {
        return new Gson();
    }
}
