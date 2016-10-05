package de.axelspringer.ideas.tools.dash.business.jenkins.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * If you archive an artifact named {@link #JENKINS_BUILD_INFO_FILE_NAME} in your jenkins job, it will be parsed and used
 * for meta-info like tooltips
 */
public class BuildInfo {

    public final static String JENKINS_BUILD_INFO_FILE_NAME = "build_info.json";

    private Map<String, String> stageDescriptions;

    public BuildInfo() {
        stageDescriptions = new HashMap<>();
    }

    public BuildInfo(Map<String, String> stageDescriptions) {
        this.stageDescriptions = stageDescriptions;
    }

    public Map<String, String> getStageDescriptions() {
        return stageDescriptions;
    }

    public void setStageDescriptions(Map<String, String> stageDescriptions) {
        this.stageDescriptions = stageDescriptions;
    }
}
