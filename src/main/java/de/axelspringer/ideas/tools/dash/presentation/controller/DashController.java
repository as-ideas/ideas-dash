package de.axelspringer.ideas.tools.dash.presentation.controller;

import de.axelspringer.ideas.tools.dash.business.customization.Team;
import de.axelspringer.ideas.tools.dash.business.customization.TeamProvider;
import de.axelspringer.ideas.tools.dash.presentation.UIGroups;
import de.axelspringer.ideas.tools.dash.presentation.UIGroupsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.auth.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/rest/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class DashController {

    @Autowired
    private UIGroupsService UIGroupsService;

    @Autowired
    private TeamProvider teamProvider;

    @RequestMapping(value = "infos")
    public UIGroups infos() throws IOException, AuthenticationException, ExecutionException, InterruptedException, URISyntaxException {

        return UIGroupsService.groups();
    }

    @RequestMapping(value = "teams")
    public List<String> teams() {

        return teamProvider.getTeams().stream().map(Team::getTeamName).collect(Collectors.toList());
    }
}
