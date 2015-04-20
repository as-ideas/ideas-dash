package de.axelspringer.ideas.tools.dash.business.jira.rest;

import lombok.Data;

@Data
public class Assignee {
    private String self;
    private String name;
    private String emailAddress;
    private String displayName;
}
