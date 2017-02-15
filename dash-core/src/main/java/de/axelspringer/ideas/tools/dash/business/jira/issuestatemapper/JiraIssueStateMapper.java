package de.axelspringer.ideas.tools.dash.business.jira.issuestatemapper;

import de.axelspringer.ideas.tools.dash.business.jira.rest.Issue;
import de.axelspringer.ideas.tools.dash.presentation.State;

public interface JiraIssueStateMapper {

    /**
     * Maps an {@link de.axelspringer.ideas.tools.dash.business.jira.rest.Issue} to a {@link de.axelspringer.ideas.tools.dash.presentation.State}
     * @param issue The jira issue to map the state from
     * @return The mapped state
     */
    State mapToState(Issue issue);
}
