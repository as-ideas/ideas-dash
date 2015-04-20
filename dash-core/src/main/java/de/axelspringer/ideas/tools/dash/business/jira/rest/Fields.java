package de.axelspringer.ideas.tools.dash.business.jira.rest;

import de.axelspringer.ideas.tools.dash.business.jira.Priority;
import lombok.Data;

@Data
public class Fields {

    /**
     * Team
     */
    private CustomField customfield_10144;

    private Assignee assignee;

    private Priority priority;

    private IssueType issuetype;

    private IssueStatus status;

    public boolean isBug() {
        return issuetype.getName().equalsIgnoreCase("Bug");
    }

}
