package de.axelspringer.ideas.tools.dash.business.jenkins;

/**
 * Maps a jenkins check to a job name that will be displayed in the UI
 */
public interface JenkinsJobNameMapper {
    String map(JenkinsCheck check);
}