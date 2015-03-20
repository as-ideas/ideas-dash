package de.axelspringer.ideas.tools.dash.business.jira;

import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.business.customization.Team;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JiraQueryBuilder {

    private final List<String> teams = new ArrayList<>();
    private final List<String> teamsExcluded = new ArrayList<>();
    private final List<String> issueTypes = new ArrayList<>(Arrays.asList("Bug"));
    private final List<String> issueStatus = new ArrayList<>(Arrays.asList("Open", "Reopened", "'In Progress'"));
    private final List<String> notIssueStatus = new ArrayList<>();


    private final String projectName;
    private Group stage;
    private String teamFieldName;
    private boolean useOnlyCurrentSprint = false;

    private JiraQueryBuilder(String projectName) {
        this.projectName = projectName;
    }

    public static JiraQueryBuilder builder(String projectName) {
        return new JiraQueryBuilder(projectName);
    }

    public JiraQueryBuilder withStage(Group stage) {
        this.stage = stage;
        return this;
    }

    public JiraQueryBuilder withTeamFieldName(String teamFieldName) {
        this.teamFieldName = teamFieldName;
        return this;
    }

    public JiraQueryBuilder withTeam(Team team) {
        teams.add(team.getJiraTeamName());
        return this;
    }

    public JiraQueryBuilder withIssueStatus(String... issueStatus) {
        this.issueStatus.clear();
        this.issueStatus.addAll(Arrays.asList(issueStatus));
        return this;
    }

    public JiraQueryBuilder withNotIssueStatus(String... issueStatus) {
        this.notIssueStatus.clear();
        this.notIssueStatus.addAll(Arrays.asList(issueStatus));
        return this;
    }

    public JiraQueryBuilder withIssueType(String... issueType) {
        this.issueTypes.clear();
        this.issueTypes.addAll(Arrays.asList(issueType));
        return this;
    }

    public JiraQueryBuilder withCurrentSprint() {
        this.useOnlyCurrentSprint = true;
        return this;
    }


    public JiraQueryBuilder withoutTeam(Team team) {
        teamsExcluded.add(team.getJiraTeamName());
        return this;
    }

    public String build() {
        StringBuilder queryBuilder = new StringBuilder("project = " + projectName);
        queryBuilder.append(" and issuetype in (")
                .append(StringUtils.join(issueTypes, ", "))
                .append(")");
        if (stage != null) {
            queryBuilder
                    .append(" and 'Stage ' = ")
                    .append(stage.getJiraName());
        }
        queryBuilder.append(" and status in (")
                .append(StringUtils.join(issueStatus, ", "))
                .append(")");
        if (notIssueStatus.size() > 0) {
            queryBuilder.append(" and status not in (")
                    .append(StringUtils.join(notIssueStatus, ", "))
                    .append(")");
        }


        if (teams.size() > 0) {
            if (StringUtils.isBlank(teamFieldName)) {
                throw new IllegalStateException("teamFieldName must be set for this operation. please set using withTeamFieldName()");
            }
            queryBuilder
                    .append(" and '").append(teamFieldName).append("' in (")
                    .append(StringUtils.join(teams, ", "))
                    .append(")");
        }
        if (teamsExcluded.size() > 0) {
            if (StringUtils.isBlank(teamFieldName)) {
                throw new IllegalStateException("teamFieldName must be set for this operation. please set using withTeamFieldName()");
            }
            queryBuilder
                    .append(" and '").append(teamFieldName).append("' not in (")
                    .append(StringUtils.join(teamsExcluded, ", "))
                    .append(")");
        }
        if (useOnlyCurrentSprint) {
            queryBuilder.append(" and Sprint in openSprints()");
        }

        return queryBuilder.toString();
    }

}
