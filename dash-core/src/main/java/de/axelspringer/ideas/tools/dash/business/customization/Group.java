package de.axelspringer.ideas.tools.dash.business.customization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Group {

    private final List<String> metaInfo = new ArrayList<>();

    private final boolean groupStatsEnabled;
    private final int orderScore;
    private final String jiraName;
    private final String groupId;

    public Group(boolean groupStatsEnabled, int orderScore, String jiraName, String groupId) {
        this.groupStatsEnabled = groupStatsEnabled;
        this.orderScore = orderScore;
        this.jiraName = jiraName;
        this.groupId = groupId;
    }

    public String getGroupId() {
        return groupId;
    }

    ;

    public String getJiraName() {
        return jiraName;
    }

    ;

    public int getOrderScore() {
        return orderScore;
    }

    ;

    public boolean groupStatsEnabled() {
        return groupStatsEnabled;
    }

    public Group withMetaInfo(String metaInfo) {
        this.metaInfo.add(metaInfo);
        return this;
    }

    public List<String> getMetaInfo() {
        return Collections.unmodifiableList(metaInfo);
    }

    @Override
    public String toString() {
        return getGroupId();
    }
}
