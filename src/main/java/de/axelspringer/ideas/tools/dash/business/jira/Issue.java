package de.axelspringer.ideas.tools.dash.business.jira;

import lombok.Data;

@Data
public class Issue {

    private String key;

    private Fields fields;
}
