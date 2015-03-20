package de.axelspringer.ideas.tools.dash.business.customization;

public interface Group {
    String getStageId();

    // FIXME move to PcpStage
    String getLoadbalancerHost();

    // FIXME move to PcpStage
    String getLoadbalancerHostWs();

    String getJiraName();
}
