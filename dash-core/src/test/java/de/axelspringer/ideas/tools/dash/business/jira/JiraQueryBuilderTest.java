package de.axelspringer.ideas.tools.dash.business.jira;

import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.business.customization.Team;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JiraQueryBuilderTest {

    @Test
    public void testBuildSAPOnUAT() throws Exception {

        String query = JiraQueryBuilder.builder("PCP")
                .withTeam(createTeam("SAP"))
                .withStage(createStage("PT"))
                .withTeamFieldName("PCP Team")
                .build();

        String expectedQuery = "project = 'PCP' and issuetype in (Bug) and 'Stage ' = PT and 'PCP Team' in ('SAP')";

        assertEquals(expectedQuery, query);
    }

    private Group createStage(String stage) {
        return new Group(false, 0, stage, stage);
    }

    private Team createTeam(String team) {
        return new Team() {
            @Override
            public String getTeamName() {
                return team;
            }

            @Override
            public String getJiraTeamName() {
                return "'" + team + "'";
            }
        };
    }

    @Test
    public void testBuildNoTeam() throws Exception {

        String query = JiraQueryBuilder.builder("PCP")
                .withStage(createStage("PROD"))
                .withTeamFieldName("PCP Team").build();

        String expectedQuery = "project = 'PCP' and issuetype in (Bug) and 'Stage ' = PROD";

        assertEquals(expectedQuery, query);
    }

    @Test
    public void testWithStatus() {
        String query = JiraQueryBuilder.builder("PCP")
                .withStage(createStage("PROD"))
                .withTeamFieldName("PCP Team")
                .withIssueStatus("foo")
                .build();

        String expectedQuery = "project = 'PCP' and issuetype in (Bug) and 'Stage ' = PROD and status in (foo)";

        assertEquals(expectedQuery, query);
    }
}