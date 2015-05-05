package de.axelspringer.ideas.tools.dash.business.jira;

import com.google.gson.Gson;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class JiraSerializationTest {

    private static final String PATH_TO_JSON_ANSWER = "/jira-json-answer.json";

    private Gson gson = new Gson();

    @Test
    public void desirialisationOfJenkinsAnswer() throws Exception {

        String text = new String(Files.readAllBytes(Paths.get(getClass().getResource(PATH_TO_JSON_ANSWER).toURI())), StandardCharsets.UTF_8);
        SearchResult searchResult = gson.fromJson(text, SearchResult.class);
        assertThat(searchResult.getIssues().size(), is(30));
    }
}