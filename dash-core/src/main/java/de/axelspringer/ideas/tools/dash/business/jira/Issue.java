package de.axelspringer.ideas.tools.dash.business.jira;

import lombok.Data;

@Data
public class Issue {

    private String key;

    private Fields fields;

    public boolean isBug() {
        return fields.isBug();
    }
}
