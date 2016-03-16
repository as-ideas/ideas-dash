package de.axelspringer.ideas.tools.dash.business.github;

import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.check.CheckExecutor;
import de.axelspringer.ideas.tools.dash.business.check.CheckResult;
import de.axelspringer.ideas.tools.dash.presentation.State;
import de.axelspringer.ideas.tools.dash.util.CloseableHttpClientRestClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class GithubCheckExecutor implements CheckExecutor<GithubCheck> {

    private static final Logger LOG = LoggerFactory.getLogger(GithubCheckExecutor.class);

    // This is also the max size from github!
    public static final int DEFAULT_PAGE_SIZE = 100;

    // curl -u CanardSauvage:d280154eb8138e503707700bda4522695eb3a1ee "https://api.github.com/users/as-ideas/repos?access_token=d280154eb8138e503707700bda4522695eb3a1ee" | grep full_name
    // curl -u CanardSauvage:d280154eb8138e503707700bda4522695eb3a1ee "https://api.github.com/orgs/as-ideas?access_token=d280154eb8138e503707700bda4522695eb3a1ee"
    // curl -u CanardSauvage:d280154eb8138e503707700bda4522695eb3a1ee "https://api.github.com/orgs/as-ideas/repos?per_page=125&access_token=d280154eb8138e503707700bda4522695eb3a1ee" | grep full_name

    @Autowired
    private CloseableHttpClient closeableHttpClient;

    @Override
    public List<CheckResult> executeCheck(GithubCheck check) {

        LOG.info("Executing Github-Check: " + check.getName() + " for Github-Account " + check.githubFullyQualifiedName());
        List<CheckResult> checkResults = new ArrayList<>();

        for (GithubRepo repo : filterRepos(readRepos(check), check.regexForMatchingRepoNames())) {

            List<GithubPullRequest> githubPullRequests = Arrays.asList(readPullRequests(check.githubConfig(), repo));

            if (githubPullRequests.size() > 0) {
                LOG.info(repo.name + ": zero pull requests");
            }

            // ugly :)
            if (StringUtils.hasLength(check.getFilterKeyword())) {
                githubPullRequests = githubPullRequests.stream().filter(githubPullRequest -> githubPullRequest.title.toLowerCase().contains(check.getFilterKeyword().toLowerCase())).collect(Collectors.toList());
            }

            for (GithubPullRequest pullRequest : githubPullRequests) {
                String assigneeName = pullRequest.assignee == null ? "?" : pullRequest.assignee.login;
                State state = stateOfPullRequest(pullRequest);
                final CheckResult checkResult = new CheckResult(state, "[" + repo.name + "]" + pullRequest.title, "[" + assigneeName + "]", 1, 1, check.getGroup());
                checkResult.withLink(pullRequest.html_url);
                checkResult.withTeams(check.getTeams());
                checkResults.add(checkResult);
            }
        }
        LOG.info("Finished Github-Check." + checkResults.size());
        return checkResults;
    }

    private List<GithubRepo> filterRepos(List<GithubRepo> githubRepos, String regexForMatchingRepoNames) {
        List<GithubRepo> result = new ArrayList<>();

        Pattern pattern = Pattern.compile(regexForMatchingRepoNames);
        for (GithubRepo githubRepo : githubRepos) {
            Matcher matcher = pattern.matcher(githubRepo.name);
            if (matcher.matches()) {
                result.add(githubRepo);
            }
        }
        LOG.info("Found " + result.size() + " matching Repos for RegEx '" + regexForMatchingRepoNames + "'");
        return result;
    }

    private State stateOfPullRequest(GithubPullRequest pullRequest) {
        return pullRequest == null ? State.RED : State.YELLOW;
    }

    /**
     * HINT: The maximum page size seems to be 100. So we need to iterate over all pages.
     *
     * @param check
     */
    private List<GithubRepo> readRepos(GithubCheck check) {
        GithubConfig githubConfig = check.githubConfig();

        final CloseableHttpClientRestClient restClient = new CloseableHttpClientRestClient(closeableHttpClient)
                .withCredentials(githubConfig.githubUserName(), githubConfig.githubUserPassword())
                .withQueryParameter("per_page", "" + DEFAULT_PAGE_SIZE);


        List<GithubRepo> result = new ArrayList<>();

        boolean hasNextPage = true;
        int currentPage = 1;
        while (hasNextPage) {
            restClient.setQueryParameter("page", "" + currentPage);

            GithubRepo[] tempResult = restClient.get("https://api.github.com/" + check.githubFullyQualifiedName() + "/repos", GithubRepo[].class);
            result.addAll(Arrays.asList(tempResult));

            hasNextPage = tempResult.length == DEFAULT_PAGE_SIZE;
            currentPage++;
        }

        return result;
    }

    private GithubPullRequest[] readPullRequests(GithubConfig githubConfig, GithubRepo repo) {
        final CloseableHttpClientRestClient restClient = new CloseableHttpClientRestClient(closeableHttpClient)
                .withCredentials(githubConfig.githubUserName(), githubConfig.githubUserPassword());

        return restClient.get(repo.url + "/pulls", GithubPullRequest[].class);
    }

    @Override
    public boolean isApplicable(Check check) {
        return check instanceof GithubCheck;
    }


}
