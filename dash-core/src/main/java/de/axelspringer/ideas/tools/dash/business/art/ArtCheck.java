package de.axelspringer.ideas.tools.dash.business.art;

import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.business.customization.Team;
import org.apache.http.HttpHost;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArtCheck extends Check {

    private final String url;

    private final HttpHost proxy;

    private List<String> ignoredTestSuites = new ArrayList<>();

    public ArtCheck(String name, String url, HttpHost proxy, Group group, List<Team> teams) {
        super(name, group, teams);
        this.url = url;
        this.proxy = proxy;
    }

    public String getUrl() {
        return url;
    }

    public HttpHost getProxy() {
        return proxy;
    }

    public List<String> getIgnoredTestSuites() {
        return Collections.unmodifiableList(ignoredTestSuites);
    }

    public ArtCheck ignoreTestSuite(String testName) {
        ignoredTestSuites.add(testName);
        return this;
    }
}
