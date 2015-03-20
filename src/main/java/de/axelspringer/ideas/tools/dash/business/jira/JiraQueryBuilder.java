package de.axelspringer.ideas.tools.dash.business.jira;

import de.axelspringer.ideas.tools.dash.business.customization.Stage;
import de.axelspringer.ideas.tools.dash.business.customization.Team;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class JiraQueryBuilder {

    private final List<String> teams = new ArrayList<>();
    private final List<String> teamsExcluded = new ArrayList<>();
    private final String projectName;
    private Stage stage;
    private String teamFieldName;

    private JiraQueryBuilder(String projectName) {
        this.projectName = projectName;
    }

    public static JiraQueryBuilder builder(String projectName) {
        return new JiraQueryBuilder(projectName);
    }

    public JiraQueryBuilder withStage(Stage stage) {
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

    public JiraQueryBuilder withoutTeam(Team team) {
        teamsExcluded.add(team.getJiraTeamName());
        return this;
    }

    public String build() {

        StringBuilder queryBuilder = new StringBuilder("project = " + projectName + " and issuetype in (Bug)");
        if (stage != null) {
            queryBuilder
                    .append(" and 'Stage ' = ")
                    .append(stage.getJiraName());
        }
        queryBuilder.append(" and status in (Open, Reopened, 'In Progress')");
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

        return queryBuilder.toString();
    }

}
