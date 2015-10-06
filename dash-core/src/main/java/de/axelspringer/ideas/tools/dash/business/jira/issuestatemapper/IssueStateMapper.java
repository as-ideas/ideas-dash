package de.axelspringer.ideas.tools.dash.business.jira.issuestatemapper;

import de.axelspringer.ideas.tools.dash.business.jira.rest.Issue;
import de.axelspringer.ideas.tools.dash.presentation.State;

public interface IssueStateMapper {

    /**
     * Maps an {@link de.axelspringer.ideas.tools.dash.business.jira.rest.Issue} to a {@link de.axelspringer.ideas.tools.dash.presentation.State}
     */
    State mapToState(Issue issue);
}
