package de.axelspringer.ideas.tools.dash.business.jira;

import com.google.gson.Gson;
import de.axelspringer.ideas.tools.dash.business.jira.rest.Issue;
import org.junit.Test;

import java.io.InputStream;
import java.io.InputStreamReader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class JiraSearchResultSerializationTest {

    private static final String PATH_TO_JSON_ANSWER = "/jira-json-answer.json";

    private Gson gson = new Gson();

    @Test
    public void shouldDeserializeSearchResultJson() throws Exception {
        InputStream response = getClass().getResourceAsStream(PATH_TO_JSON_ANSWER);
        SearchResult searchResult = gson.fromJson(new InputStreamReader(response), SearchResult.class);
        assertThat(searchResult.getIssues().size(), is(30));
    }

    @Test
    public void shouldParseIssueCreationDate() throws Exception {
        InputStream response = getClass().getResourceAsStream(PATH_TO_JSON_ANSWER);
        SearchResult searchResult = gson.fromJson(new InputStreamReader(response), SearchResult.class);
        for (Issue issue : searchResult.getIssues()) {
            //Issue creation date is mandatory in JIRA so should never be null
            assertThat(issue.getFields().getCreatedAtDateTime(), notNullValue());
        }
    }
}