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

    private Boolean deleted = false;

    public CheckResultComment() {
    }

    public CheckResultComment(String checkResultIdentifier, String comment) {
        this.checkResultIdentifier = checkResultIdentifier;
        this.comment = comment;
    }

    public CheckResultComment(String commentIdentifier, long creationTime, String checkResultIdentifier, String comment, Boolean deleted) {
        this.commentIdentifier = commentIdentifier;
        this.creationTime = creationTime;
        this.checkResultIdentifier = checkResultIdentifier;
        this.comment = comment;
        this.deleted = deleted;
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

    public void setCommentIdentifier(String commentIdentifier) {
        this.commentIdentifier = commentIdentifier;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public void setCheckResultIdentifier(String checkResultIdentifier) {
        this.checkResultIdentifier = checkResultIdentifier;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
