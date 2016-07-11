package de.axelspringer.ideas.tools.dash.business.customization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractGroup implements Group {

    private final List<String> metaInfo = new ArrayList<>();

    @Override
    public Group withMetaInfo(String metaInfo) {
        this.metaInfo.add(metaInfo);
        return this;
    }

    @Override
    public List<String> getMetaInfo() {
        return Collections.unmodifiableList(metaInfo);
    }
}
