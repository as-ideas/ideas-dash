package de.axelspringer.ideas.tools.dash.business.jira.issuestatemapper;

import de.axelspringer.ideas.tools.dash.business.jira.rest.Issue;
import de.axelspringer.ideas.tools.dash.business.jira.rest.Priority;
import de.axelspringer.ideas.tools.dash.presentation.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DefaultJiraIssueStateMapper implements JiraIssueStateMapper {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public State mapToState(Issue issue) {

        return issue.isBug() ? forBug(issue) : forIssue(issue);
    }

    protected State forIssue(Issue issue) {

        return  isStatusDone(issue) ? State.GREEN : State.YELLOW;
    }

    protected State forBug(Issue issue) {

        if (isPriorityBlocker(issue)) {
            return State.RED;
        }

        if (isStatusDone(issue)) {
            return State.GREEN;
        }
        return State.YELLOW;
    }

    protected boolean isStatusDone(Issue issue) {

        return issue.getFields().getStatus().getName().toLowerCase().equals("done");
    }

    protected boolean isPriorityBlocker(Issue issue) {

        Priority jiraTicketPriority = issue.getFields().getPriority();

        if (jiraTicketPriority == null) {
            log.error("Priority is not set! Issue: " + issue.getKey());
            return false;
        }


        String priority = jiraTicketPriority.getName();
        return Priority.BLOCKER_NAME.equals(priority);
    }
}
