package de.axelspringer.ideas.tools.dash.business.check.checkresult;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Serves as a non-persisted {@link CheckResultComment} - cache/repository
 */
@Component
public class CheckResultCommentRepository {

    private final static long COMMENT_TIME_TO_LIVE = Duration.ofDays(30).toMillis();

    private final Map<String, CheckResultComment> comments = new ConcurrentHashMap<>();

    public void addComments(List<CheckResultComment> comments) {

        comments.forEach(this::saveComment);
    }

    public List<CheckResultComment> comments() {

        List<CheckResultComment> commentsAsList = new ArrayList<>();
        commentsAsList.addAll(comments.values());
        return Collections.unmodifiableList(commentsAsList);
    }

    private CheckResultCommentRepository saveComment(CheckResultComment comment) {

        // deleted = true if deleted on server OR on client
        boolean deleted = comment.getDeleted() ||
                Optional.ofNullable(comments.get(comment.getCommentIdentifier()))
                        .map(CheckResultComment::getDeleted)
                        .orElse(false);
        comment.setDeleted(deleted);

        // save or update
        comments.put(comment.getCommentIdentifier(), comment);

        // cleanup old comments
        performMaintenance();

        return this;
    }

    /**
     * Removes all comments that are older than {@link #COMMENT_TIME_TO_LIVE}
     */
    private void performMaintenance() {

        comments.forEach((identifier, comment) -> {
            if (comment.getCreationTime() + COMMENT_TIME_TO_LIVE < System.currentTimeMillis()) {
                comments.remove(identifier);
            }
        });
    }
}
