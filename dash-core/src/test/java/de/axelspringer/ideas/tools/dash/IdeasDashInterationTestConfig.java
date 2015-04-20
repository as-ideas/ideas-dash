package de.axelspringer.ideas.tools.dash;

import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.check.CheckProvider;
import de.axelspringer.ideas.tools.dash.business.customization.Team;
import de.axelspringer.ideas.tools.dash.business.customization.TeamProvider;
import de.axelspringer.ideas.tools.dash.config.ClientConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ComponentScan(basePackages = "de.axelspringer.ideas.tools.dash", excludeFilters = @ComponentScan.Filter(Configuration.class))
@Import(ClientConfig.class)
public class IdeasDashInterationTestConfig {

    @Bean
    public MockCheckProvider mockCheckProvider() {
        return new MockCheckProvider();
    }

    @Bean
    public MockTeamProvider mockTeamProvider() {
        return new MockTeamProvider();
    }

    public static class MockCheckProvider implements CheckProvider {

        @Override
        public List<Check> provideChecks() {
            return new ArrayList<>();
        }
    }

    public static class MockTeamProvider implements TeamProvider {

        @Override
        public List<Team> getTeams() {
            return new ArrayList<>();
        }
    }

}
