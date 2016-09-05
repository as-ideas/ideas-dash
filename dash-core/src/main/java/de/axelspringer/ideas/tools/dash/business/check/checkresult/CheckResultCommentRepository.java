package de.axelspringer.ideas.tools.dash.business.check.checkresult;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Serves as a non-persisted {@link CheckResultComment} - cache/repository
 */
@Component
public class CheckResultCommentRepository {

    private final static int COMMENT_CACHE_SIZE = 100;

    private final List<CheckResultComment> comments = new ArrayList<>();

    public void addComments(List<CheckResultComment> comments) {
        comments.forEach(this::addComment);
    }

    public List<CheckResultComment> comments() {
        return Collections.unmodifiableList(comments);
    }

    private CheckResultCommentRepository addComment(CheckResultComment comment) {
        if (findCommentById(comment.getCommentIdentifier()) == null) {
            comments.add(comment);
        }
        if (comments.size() > COMMENT_CACHE_SIZE) {
            comments.remove(0);
        }
        return this;
    }

    private CheckResultComment findCommentById(String id) {
        return comments.stream()
                .filter(cachedComment -> cachedComment.getCommentIdentifier().equals(id))
                .findAny()
                .orElse(null);
    }
}
