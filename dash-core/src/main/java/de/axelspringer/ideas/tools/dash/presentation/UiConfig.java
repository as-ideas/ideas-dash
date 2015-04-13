package de.axelspringer.ideas.tools.dash.presentation;

import lombok.Data;

@Data
public class UiConfig {

    private final String title;

    public UiConfig(String title) {
        this.title = title;
    }
}
