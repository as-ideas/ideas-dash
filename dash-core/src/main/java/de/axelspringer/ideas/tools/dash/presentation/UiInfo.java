package de.axelspringer.ideas.tools.dash.presentation;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UiInfo {

    private final String applicationStartId;
    private String lastUpdateTime = "N/A";
    private List<UIGroup> groups = new ArrayList<>();

    public UiInfo(String applicationStartId) {
        this.applicationStartId = applicationStartId;
    }

    public void add(UIGroup uiGroup) {
        groups.add(uiGroup);
    }
}
