package de.axelspringer.ideas.tools.dash.business.jenkins.joblist;

import de.axelspringer.ideas.tools.dash.business.jenkins.job.JenkinsJobCheck;

/**
 * Maps a jenkins check to a job name that will be displayed in the UI
 */
public interface JenkinsJobNameMapper {
    String map(JenkinsJobCheck check);
}