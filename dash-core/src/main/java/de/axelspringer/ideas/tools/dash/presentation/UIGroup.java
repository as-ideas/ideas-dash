package de.axelspringer.ideas.tools.dash.presentation;

import de.axelspringer.ideas.tools.dash.business.check.CheckResult;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

public class UIGroup {

    private String name;

    private String info;

    private State state;

    private int totalCount = 0;

    private int failCount = 0;

    private Collection<CheckResult> checks = new LinkedHashSet<>();

    private int orderScore = -1;

    public UIGroup() {
    }

    public void add(CheckResult checkResult) {
        checks.addAll(Arrays.asList(checkResult));
    }

    public void add(List<CheckResult> checkResults) {
        checks.addAll(checkResults);
    }

    public String getName() {
        return this.name;
    }

    public String getInfo() {
        return this.info;
    }

    public State getState() {
        return this.state;
    }

    public int getTotalCount() {
        return this.totalCount;
    }

    public int getFailCount() {
        return this.failCount;
    }

    public List<CheckResult> getChecks() {
        return Collections.unmodifiableList(new ArrayList<>(this.checks));
    }

    public int getOrderScore() {
        return this.orderScore;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }

    public void setChecks(List<CheckResult> checks) {
        this.checks = checks;
    }

    public void setOrderScore(int orderScore) {
        this.orderScore = orderScore;
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
