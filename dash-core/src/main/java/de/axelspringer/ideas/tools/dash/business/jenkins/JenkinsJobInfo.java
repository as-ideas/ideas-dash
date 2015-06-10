package de.axelspringer.ideas.tools.dash.business.jenkins;

public class JenkinsJobInfo {

    private LastBuild lastCompletedBuild;

    private LastBuild lastBuild;

    public JenkinsJobInfo() {
    }

    public LastBuild getLastCompletedBuild() {
        return this.lastCompletedBuild;
    }

    public LastBuild getLastBuild() {
        return this.lastBuild;
    }

    public void setLastCompletedBuild(LastBuild lastCompletedBuild) {
        this.lastCompletedBuild = lastCompletedBuild;
    }

    public void setLastBuild(LastBuild lastBuild) {
        this.lastBuild = lastBuild;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof JenkinsJobInfo)) {
            return false;
        }
        final JenkinsJobInfo other = (JenkinsJobInfo) o;
        if (!other.canEqual((Object) this)) {
            return false;
        }
        final Object this$lastCompletedBuild = this.lastCompletedBuild;
        final Object other$lastCompletedBuild = other.lastCompletedBuild;
        if (this$lastCompletedBuild == null ? other$lastCompletedBuild != null : !this$lastCompletedBuild.equals(other$lastCompletedBuild)) {
            return false;
        }
        final Object this$lastBuild = this.lastBuild;
        final Object other$lastBuild = other.lastBuild;
        if (this$lastBuild == null ? other$lastBuild != null : !this$lastBuild.equals(other$lastBuild)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $lastCompletedBuild = this.lastCompletedBuild;
        result = result * PRIME + ($lastCompletedBuild == null ? 0 : $lastCompletedBuild.hashCode());
        final Object $lastBuild = this.lastBuild;
        result = result * PRIME + ($lastBuild == null ? 0 : $lastBuild.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof JenkinsJobInfo;
    }

    public String toString() {
        return "de.axelspringer.ideas.tools.dash.business.jenkins.JenkinsJobInfo(lastCompletedBuild=" + this.lastCompletedBuild + ", lastBuild=" + this.lastBuild + ")";
    }

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
