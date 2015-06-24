package de.axelspringer.ideas.tools.dash.presentation.controller;

import de.axelspringer.ideas.tools.dash.presentation.UiConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UiConfigState {


    @Value("${de.axelspringer.ideas.tools.dash.pagetitle:angel dust}")
    private String title;

    private UiConfig uiConfig;

    public UiConfig get() {
        if(uiConfig == null) {
            uiConfig = createUiConfig();
        }
        return uiConfig;
    }

    private UiConfig createUiConfig() {
        return new UiConfig(title);
    }
}
