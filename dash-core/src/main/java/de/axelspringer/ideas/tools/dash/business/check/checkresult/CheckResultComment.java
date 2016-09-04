package de.axelspringer.ideas.tools.dash.business.check.checkresult;

import java.util.UUID;

/**
 * Represents a comment mapped to a check result
 */
public class CheckResultComment {

    private final String commentIdentifier = UUID.randomUUID().toString();

    private final String checkResultIdentifier;

    private final String comment;

    public CheckResultComment(String checkResultIdentifier, String comment) {
        this.checkResultIdentifier = checkResultIdentifier;
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public String getCommentIdentifier() {
        return commentIdentifier;
    }

    public String getCheckResultIdentifier() {
        return checkResultIdentifier;
    }
}
