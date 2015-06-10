package de.axelspringer.ideas.tools.dash.business.jira.rest;

public class CustomField {

    private String value;

    public CustomField() {
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CustomField)) {
            return false;
        }
        final CustomField other = (CustomField) o;
        if (!other.canEqual((Object) this)) {
            return false;
        }
        final Object this$value = this.value;
        final Object other$value = other.value;
        if (this$value == null ? other$value != null : !this$value.equals(other$value)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $value = this.value;
        result = result * PRIME + ($value == null ? 0 : $value.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof CustomField;
    }

    public String toString() {
        return "de.axelspringer.ideas.tools.dash.business.jira.rest.CustomField(value=" + this.value + ")";
    }
}
