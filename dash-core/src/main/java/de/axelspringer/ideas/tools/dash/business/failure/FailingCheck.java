package de.axelspringer.ideas.tools.dash.business.failure;

import de.axelspringer.ideas.tools.dash.business.check.AbstractCheck;

public class FailingCheck extends AbstractCheck {

    private final String failureMessage;

    public FailingCheck(String name, String failureMessage) {
        super(name, FailureGroup.INSTANCE, null);
        this.failureMessage = failureMessage;
    }

    public String getFailureMessage() {
        return this.failureMessage;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FailingCheck)) {
            return false;
        }
        final FailingCheck other = (FailingCheck) o;
        if (!other.canEqual((Object) this)) {
            return false;
        }
        final Object this$failureMessage = this.failureMessage;
        final Object other$failureMessage = other.failureMessage;
        if (this$failureMessage == null ? other$failureMessage != null : !this$failureMessage.equals(other$failureMessage)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $failureMessage = this.failureMessage;
        result = result * PRIME + ($failureMessage == null ? 0 : $failureMessage.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof FailingCheck;
    }

    public String toString() {
        return "de.axelspringer.ideas.tools.dash.business.failure.FailingCheck(failureMessage=" + this.failureMessage + ")";
    }
}
