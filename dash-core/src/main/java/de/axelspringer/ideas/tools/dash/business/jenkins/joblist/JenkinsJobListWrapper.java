package de.axelspringer.ideas.tools.dash.business.jenkins.joblist;


import de.axelspringer.ideas.tools.dash.business.jenkins.domain.JenkinsElement;

import java.util.List;

public class JenkinsJobListWrapper {

    private List<JenkinsElement> jobs;

    public List<JenkinsElement> getJobs() {
        return jobs;
    }

    public void setJobs(List<JenkinsElement> jobs) {
        this.jobs = jobs;
    }
}
