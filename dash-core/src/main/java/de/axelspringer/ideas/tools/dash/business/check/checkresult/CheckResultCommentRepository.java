package de.axelspringer.ideas.tools.dash.business.check.checkresult;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Serves as a non-persisted {@link CheckResultComment} - cache/repository
 * <p>
 * TODO: think about cleanup - we do not want this to grow forever in memory
 */
@Component
public class CheckResultCommentRepository {

    private final List<CheckResultComment> comments = new ArrayList<>();

    public CheckResultCommentRepository addComment(CheckResultComment comment) {
        comments.add(comment);
        ;
        return this;
    }

    public List<CheckResultComment> comments() {
        return Collections.unmodifiableList(comments);
    }
}
