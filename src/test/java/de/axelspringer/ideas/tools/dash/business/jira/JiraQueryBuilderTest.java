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

        String expectedQuery = "project = PCP and issuetype in (Bug) and 'Stage ' = PT and status in (Open, Reopened, 'In Progress') and 'PCP Team' in ('SAP')";

        assertEquals(expectedQuery, query);
    }

    private Group createStage(String stage) {
        return new Group() {
            @Override
            public String getStageId() {
                return stage;
            }

            @Override
            public String getLoadbalancerHost() {
                return stage;
            }

            @Override
            public String getLoadbalancerHostWs() {
                return stage;
            }

            @Override
            public String getJiraName() {
                return stage;
            }
        };
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

        String expectedQuery = "project = PCP and issuetype in (Bug) and 'Stage ' = PROD and status in (Open, Reopened, 'In Progress')";

        assertEquals(expectedQuery, query);
    }
}