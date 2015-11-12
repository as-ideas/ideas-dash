package de.axelspringer.ideas.tools.dash.business.github;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubPullRequest {

    public String url;
    public Long id;
    public String html_url;
    public String diff_url;
    public String patch_url;
    public String issue_url;
    public Long number;
    public String state;
    public Boolean locked;
    public String title;
    public GithubUser user;
    public String body;
    public String created_at;
    public String updated_at;
    public String closed_at;
    public String merged_at;
    public String merge_commit_sha;
    public GithubUser assignee;
    public String milestone;
    public String commits_url;
    public String review_comments_url;
    public String review_comment_url;
    public String comments_url;
    public String statuses_url;
}
