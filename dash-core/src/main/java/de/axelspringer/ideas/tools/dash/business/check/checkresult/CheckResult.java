package de.axelspringer.ideas.tools.dash.business.check.checkresult;

import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.business.customization.Team;
import de.axelspringer.ideas.tools.dash.presentation.State;

import java.util.ArrayList;
import java.util.List;

public class CheckResult {

    private State state;

    /**
     * If specified the client will use this to order checkresults in a group.
     * It will otherwise order by state...
     */
    private Integer order;

    private String name;

    private String info;

    /**
     * Will be displayed as the 'tooltip' in the UI
     */
    private String description;

    /**
     * Link for Detail info.
     */
    private String link;

    /**
     * Indicates errors of the check execution.
     * It does not mean, that the validation/check against a metric failed or was not successful.
     * In the case of errors, it is not possible to define success/failure!
     */
    private boolean isError = false;

    private int testCount = 0;

    private int failCount = 0;

    private boolean running = false;

    private String iconSrc;

    private Group group;

    private final List<String> teams = new ArrayList<>();

    /**
     * This field serves as an identifier for a check execution. The value of this field should be identical no matter
     * how often the check is executed if the item under check has not changed (for instance if the same jenkins job is checked several times.
     * It serves as an identifier for information that is mapped to a check result (eg comments).
     */
    private String checkResultIdentifier;

    public CheckResult(State state, String name, String info, int testCount, int failCount, Group group) {
        this.state = state;
        this.name = name;
        this.info = info;
        this.testCount = testCount;
        this.failCount = failCount;
        this.group = group;
    }

    public CheckResult markRunning() {
        this.running = true;
        return this;
    }

    public CheckResult withLink(String link) {
        this.link = link;
        return this;
    }

    public CheckResult withTeams(List<Team> teams) {

        if (teams != null) {
            teams.forEach(team -> {
                if (team != null) {
                    this.teams.add(team.getTeamName());
                }
            });
        }
        return this;
    }

    public CheckResult withName(String name) {
        this.name = name;
        return this;
    }

    public CheckResult withIconSrc(String link) {
        this.iconSrc = link;
        return this;
    }

    /**
     * see {@link #checkResultIdentifier}
     */
    public CheckResult withCheckResultIdentifier(String checkResultIdentifier) {
        this.checkResultIdentifier = checkResultIdentifier;
        return this;
    }

    /**
     * see {@link #checkResultIdentifier}
     */
    public String getCheckResultIdentifier() {
        return checkResultIdentifier;
    }

    public State getState() {
        return this.state;
    }

    public String getName() {
        return this.name;
    }

    public String getInfo() {
        return this.info;
    }

    public String getLink() {
        return this.link;
    }

    public boolean isError() {
        return this.isError;
    }

    public int getTestCount() {
        return this.testCount;
    }

    public int getFailCount() {
        return this.failCount;
    }

    public boolean isRunning() {
        return this.running;
    }

    public String getIconSrc() {
        return iconSrc;
    }

    public Group getGroup() {
        return this.group;
    }

    public List<String> getTeams() {
        return this.teams;
    }

    public Integer getOrder() {
        return order;
    }

    public CheckResult withOrder(Integer order) {
        this.order = order;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public CheckResult withDescription(String description) {
        this.description = description;
        return this;
    }
}
