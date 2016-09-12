package de.axelspringer.ideas.tools.dash.business.check.checkresult;

import java.util.UUID;

/**
 * Represents a comment mapped to a check result
 */
public class CheckResultComment {

    private String commentIdentifier = UUID.randomUUID().toString();

    private long creationTime = System.currentTimeMillis();

    private String checkResultIdentifier;

    private String comment;

    public CheckResultComment() {
    }

    public CheckResultComment(String checkResultIdentifier, String comment) {
        this.checkResultIdentifier = checkResultIdentifier;
        this.comment = comment;
    }

    public CheckResultComment(String commentIdentifier, long creationTime, String checkResultIdentifier, String comment) {
        this.commentIdentifier = commentIdentifier;
        this.creationTime = creationTime;
        this.checkResultIdentifier = checkResultIdentifier;
        this.comment = comment;
    }

    public long getCreationTime() {
        return creationTime;
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
