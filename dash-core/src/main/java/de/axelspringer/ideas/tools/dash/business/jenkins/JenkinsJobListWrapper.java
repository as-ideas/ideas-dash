package de.axelspringer.ideas.tools.dash.business.jenkins;


import java.util.List;

public class JenkinsJobListWrapper {

    private List<JenkinsJob> jobs;

    public List<JenkinsJob> getJobs() {
        return jobs;
    }

    public void setJobs(List<JenkinsJob> jobs) {
        this.jobs = jobs;
    }
}
