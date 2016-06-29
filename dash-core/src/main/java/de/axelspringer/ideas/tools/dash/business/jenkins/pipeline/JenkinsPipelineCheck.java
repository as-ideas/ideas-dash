package de.axelspringer.ideas.tools.dash.business.jenkins.pipeline;

import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.business.customization.Team;
import de.axelspringer.ideas.tools.dash.business.jenkins.JenkinsServerConfiguration;
import de.axelspringer.ideas.tools.dash.business.jenkins.job.JenkinsJobCheck;

import java.util.List;

public class JenkinsPipelineCheck extends JenkinsJobCheck {

    public JenkinsPipelineCheck(JenkinsServerConfiguration serverConfig, String pipelineName, Group group, List<Team> teams) {
        super(pipelineName, serverConfig, serverConfig.getUrl() + "/job/" + pipelineName, group, teams, null);
    }
}
