package de.axelspringer.ideas.tools.dash.presentation;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UiInfo {

    private String lastUpdateTime = "N/A";

    private List<UIGroup> groups = new ArrayList<>();

    public void add(UIGroup uiGroup) {
        groups.add(uiGroup);
    }
}
