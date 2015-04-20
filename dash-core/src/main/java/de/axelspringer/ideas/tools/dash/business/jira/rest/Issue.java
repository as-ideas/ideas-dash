package de.axelspringer.ideas.tools.dash.business.jira.rest;

import lombok.Data;

@Data
public class Issue {

    private String key;

    private Fields fields;

    public boolean isBug() {
        return fields.isBug();
    }

    public IssueType getIssueType() {
        return getFields().getIssuetype();
    }

    public IssueStatus getIssueStatus() {
        return getFields().getStatus();
    }
}
