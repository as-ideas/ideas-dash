package de.axelspringer.ideas.tools.dash.presentation.controller;

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
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/rest/groups")
@Slf4j
public class StageController {

    @Autowired
    private UIGroupsService UIGroupsService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public UIGroups getStageInfos() throws IOException, AuthenticationException, ExecutionException, InterruptedException, URISyntaxException {

        return UIGroupsService.groups();
    }
}
