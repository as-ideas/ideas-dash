package de.axelspringer.ideas.tools.dash.business.stash;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@AllArgsConstructor
@Component
public class StashConfig {

    @Getter
    @Value("${dash.stash.repo-url:false}")
    private boolean enabled;
    @Getter
    @Value("${dash.stash.repo-url:}")
    private String repoUrl;
    @Getter
    @Value("${dash.stash.username:}")
    private String username;
    @Getter
    @Value("${dash.stash.password:}")
    private String password;

    public String pullRequestUrl(String repoName) {
        return repoUrl + StringUtils.prependIfMissing(repoName, "/") + "/pull-requests?state=OPEN";
    }

    public String pullRequestAccessUrl(String repoName, String id) {
        return repoUrl + StringUtils.prependIfMissing(repoName, "/") + "/pull-requests/" + id;
    }
}
