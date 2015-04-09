package de.axelspringer.ideas.tools.dash.business.jira;

import lombok.Data;

/**
 * status": {
 * "self": "https://as-jira.axelspringer.de/rest/api/2/status/1",
 * "description": "The issue is open and ready for the assignee to start work on it.",
 * "iconUrl": "https://as-jira.axelspringer.de/images/icons/statuses/open.png",
 * "name": "Open",
 * "id": "1",
 * "statusCategory": {
 * "self": "https://as-jira.axelspringer.de/rest/api/2/statuscategory/2",
 * "id": 2,
 * "key": "new",
 * "colorName": "blue-gray",
 * "name": "New"
 * }
 * },
 */
@Data
public class IssueStatus {
    private String name;
}
