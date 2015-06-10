package de.axelspringer.ideas.tools.dash.presentation;

import de.axelspringer.ideas.tools.dash.business.check.CheckResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UIGroup {

    private String name;

    private String info;

    private State state;

    private int totalCount = 0;

    private int failCount = 0;

    private List<CheckResult> checks = new ArrayList<>();

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
        return this.checks;
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

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UIGroup)) {
            return false;
        }
        final UIGroup other = (UIGroup) o;
        if (!other.canEqual((Object) this)) {
            return false;
        }
        final Object this$name = this.name;
        final Object other$name = other.name;
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        final Object this$info = this.info;
        final Object other$info = other.info;
        if (this$info == null ? other$info != null : !this$info.equals(other$info)) {
            return false;
        }
        final Object this$state = this.state;
        final Object other$state = other.state;
        if (this$state == null ? other$state != null : !this$state.equals(other$state)) {
            return false;
        }
        if (this.totalCount != other.totalCount) {
            return false;
        }
        if (this.failCount != other.failCount) {
            return false;
        }
        final Object this$checks = this.checks;
        final Object other$checks = other.checks;
        if (this$checks == null ? other$checks != null : !this$checks.equals(other$checks)) {
            return false;
        }
        if (this.orderScore != other.orderScore) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.name;
        result = result * PRIME + ($name == null ? 0 : $name.hashCode());
        final Object $info = this.info;
        result = result * PRIME + ($info == null ? 0 : $info.hashCode());
        final Object $state = this.state;
        result = result * PRIME + ($state == null ? 0 : $state.hashCode());
        result = result * PRIME + this.totalCount;
        result = result * PRIME + this.failCount;
        final Object $checks = this.checks;
        result = result * PRIME + ($checks == null ? 0 : $checks.hashCode());
        result = result * PRIME + this.orderScore;
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof UIGroup;
    }

    public String toString() {
        return "de.axelspringer.ideas.tools.dash.presentation.UIGroup(name=" + this.name + ", info=" + this.info + ", state=" + this.state + ", totalCount=" + this.totalCount + ", failCount=" + this.failCount + ", checks=" + this.checks + ", orderScore=" + this.orderScore + ")";
    }
}
