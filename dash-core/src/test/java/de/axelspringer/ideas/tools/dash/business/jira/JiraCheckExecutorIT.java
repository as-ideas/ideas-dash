package de.axelspringer.ideas.tools.dash.business.jira;

import com.google.gson.Gson;
import de.axelspringer.ideas.tools.dash.IdeasDashInterationTestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = IdeasDashInterationTestConfig.class)
public class JiraCheckExecutorIT {

    private static final String PATH_TO_JSON_ANSWER = "/jira-json-answer.json";

    @Autowired
    private Gson gson;

    @Test
    public void desirialisationOfJenkinsAnswer() throws Exception {
        String text = new String(Files.readAllBytes(Paths.get(getClass().getResource(PATH_TO_JSON_ANSWER).toURI())), StandardCharsets.UTF_8);

        SearchResult searchResult = gson.fromJson(text, SearchResult.class);

        assertThat(searchResult.getIssues().size(), is(30));
    }
}