package de.axelspringer.ideas.tools.dash.business.jenkins;

import lombok.Data;

@Data
public class JenkinsJobInfo {

    private LastBuild lastCompletedBuild;

    private LastBuild lastBuild;

    public class LastBuild {

        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
