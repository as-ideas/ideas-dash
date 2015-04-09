package de.axelspringer.ideas.tools.dash.business.jira;

import lombok.Data;

import java.util.List;

@Data
public class SearchResult {

    private List<Issue> issues;
}
