package de.axelspringer.ideas.tools.dash.business.jira.rest;

import lombok.Data;

// eg. "issuetype":{"self": "https://as-jira.axelspringer.de/rest/api/2/issuetype/37", "id": "37", "description": "Eine User Story", "iconUrl": "https://as-jira.axelspringer.de/images/icons/issuetypes/story.png", "name": "Story", "subtask": false},
@Data
public class IssueType {
    private String self;
    private String id;
    private String description;
    private String name;
}
