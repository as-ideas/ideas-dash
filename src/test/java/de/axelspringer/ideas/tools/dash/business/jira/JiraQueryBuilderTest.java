package de.axelspringer.ideas.tools.dash.business.jira;

import de.axelspringer.ideas.tools.dash.business.customization.Team;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JiraQueryBuilderTest {

    @Test
    public void testBuildSAPOnUAT() throws Exception {

        String query = JiraQueryBuilder.builder("PCP")
                .withTeam(createTeam("SAP"))
                .withStage(JiraQueryBuilder.Stage.PT)
                .withTeamFieldName("PCP Team")
                .build();

        String expectedQuery = "project = PCP and issuetype in (Bug) and 'Stage ' = PT and status in (Open, Reopened, 'In Progress') and 'PCP Team' in ('SAP')";

        assertEquals(expectedQuery, query);
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
                .withStage(JiraQueryBuilder.Stage.PROD)
                .withTeamFieldName("PCP Team").build();

        String expectedQuery = "project = PCP and issuetype in (Bug) and 'Stage ' = PROD and status in (Open, Reopened, 'In Progress')";

        assertEquals(expectedQuery, query);
    }
}