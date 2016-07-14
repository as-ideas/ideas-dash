package de.axelspringer.ideas.tools.dash.business.customization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * Deprecated in favor of {@link AbstractGroup} as we want to be able to include meta info
 */
@Deprecated
public interface Group {

    static Logger LOG = LoggerFactory.getLogger(Group.class);

    String getGroupId();

    String getJiraName();

    int getOrderScore();

    default boolean groupStatsEnabled() {
        return true;
    }

    default Group withMetaInfo(String metaInfo) {
        LOG.error("not implemented by group");
        return this;
    }

    default List<String> getMetaInfo() {
        LOG.error("not implemented by group");
        return Collections.emptyList();
    }
}
