package de.axelspringer.ideas.tools.dash.presentation.controller;

import de.axelspringer.ideas.tools.dash.business.check.checkresult.CheckResultComment;
import de.axelspringer.ideas.tools.dash.business.check.checkresult.CheckResultCommentRepository;
import de.axelspringer.ideas.tools.dash.business.customization.Team;
import de.axelspringer.ideas.tools.dash.business.customization.TeamProvider;
import de.axelspringer.ideas.tools.dash.presentation.UiConfig;
import de.axelspringer.ideas.tools.dash.presentation.UiInfo;
import de.axelspringer.ideas.tools.dash.presentation.UiInfoService;
import de.axelspringer.ideas.tools.dash.presentation.UiTeams;
import org.apache.http.auth.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/rest")
public class DashController {

    @Autowired
    private UiInfoService UiInfoService;

    @Autowired(required = false)
    private TeamProvider teamProvider;

    @Autowired
    private UiConfigState uiConfigState;

    @Autowired
    private CheckResultCommentRepository commentRepository;

    @RequestMapping(value = "/config", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public UiConfig config() {
        return uiConfigState.get();
    }

    @RequestMapping(value = "/infos", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public UiInfo infos() throws IOException, AuthenticationException, ExecutionException, InterruptedException, URISyntaxException {
        return UiInfoService.infos();
    }

    @RequestMapping(value = "/teams", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public UiTeams teams() {
        if (teamProvider == null) {
            return UiTeams.ALL;
        }
        List<String> teams = teamProvider.getTeams().stream().map(Team::getTeamName).collect(Collectors.toList());
        return new UiTeams(teams);
    }

    @RequestMapping(value = "/comments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CheckResultComment> comments() {
        return commentRepository.comments();
    }

    @RequestMapping(value = "/comments", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void storeComment(@RequestBody ArrayList<CheckResultComment> comments) {
        commentRepository.addComments(comments);
    }
}
