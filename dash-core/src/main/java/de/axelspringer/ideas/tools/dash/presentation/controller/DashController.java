package de.axelspringer.ideas.tools.dash.presentation.controller;

import de.axelspringer.ideas.tools.dash.business.customization.Team;
import de.axelspringer.ideas.tools.dash.business.customization.TeamProvider;
import de.axelspringer.ideas.tools.dash.presentation.UiInfo;
import de.axelspringer.ideas.tools.dash.presentation.UiInfoService;
import de.axelspringer.ideas.tools.dash.presentation.UiTeams;
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
    private UiInfoService UiInfoService;

    @Autowired
    private TeamProvider teamProvider;

    @RequestMapping(value = "infos")
    public UiInfo infos() throws IOException, AuthenticationException, ExecutionException, InterruptedException, URISyntaxException {
        return UiInfoService.infos();
    }

    @RequestMapping(value = "teams")
    public UiTeams teams() {
        List<String> teams = teamProvider.getTeams().stream().map(Team::getTeamName).collect(Collectors.toList());
        return new UiTeams(teams);
    }
}
