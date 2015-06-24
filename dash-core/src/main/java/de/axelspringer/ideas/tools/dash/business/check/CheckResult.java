package de.axelspringer.ideas.tools.dash.business.check;

import de.axelspringer.ideas.tools.dash.business.customization.Group;
import de.axelspringer.ideas.tools.dash.business.customization.Team;
import de.axelspringer.ideas.tools.dash.presentation.State;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class CheckResult {

    private State state;

    private String name;

    private String info;

    /**
     * Link for Detail info.
     */
    private String link;

    /**
     * Indicates errors of the check execution.
     * It does not mean, that the validation/check against a metric failed or was not successful. In the case of errors, it is not possible to define success/failure!
     */
    private boolean isError = false;

    private int testCount = 0;

    private int failCount = 0;

    private boolean running = false;

    private Group group;

    private Team team;

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

    public CheckResult withTeam(Team team) {
        this.team = team;
        return this;
    }

    public CheckResult withName(String name) {
        this.name = name;
        return this;
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

    public Group getGroup() {
        return this.group;
    }

    public Team getTeam() {
        return this.team;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
