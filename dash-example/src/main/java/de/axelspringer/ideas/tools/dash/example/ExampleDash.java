package de.axelspringer.ideas.tools.dash.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import de.axelspringer.ideas.tools.dash.DashConfig;

@SpringBootApplication
@Import(DashConfig.class)
public class ExampleDash {

    public static void main(String[] args) {
        SpringApplication.run(ExampleDash.class, args);
    }
}
