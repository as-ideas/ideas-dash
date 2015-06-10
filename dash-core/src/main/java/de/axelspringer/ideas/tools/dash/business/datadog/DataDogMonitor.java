package de.axelspringer.ideas.tools.dash.business.datadog;

public class DataDogMonitor {

    public final static String STATE_OK = "OK";

    private String name;
    private String query;
    private String overall_state;
    private String type;

    public DataDogMonitor() {
    }

    public String getName() {
        return this.name;
    }

    public String getQuery() {
        return this.query;
    }

    public String getOverall_state() {
        return this.overall_state;
    }

    public String getType() {
        return this.type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setOverall_state(String overall_state) {
        this.overall_state = overall_state;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DataDogMonitor)) {
            return false;
        }
        final DataDogMonitor other = (DataDogMonitor) o;
        if (!other.canEqual((Object) this)) {
            return false;
        }
        final Object this$name = this.name;
        final Object other$name = other.name;
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        final Object this$query = this.query;
        final Object other$query = other.query;
        if (this$query == null ? other$query != null : !this$query.equals(other$query)) {
            return false;
        }
        final Object this$overall_state = this.overall_state;
        final Object other$overall_state = other.overall_state;
        if (this$overall_state == null ? other$overall_state != null : !this$overall_state.equals(other$overall_state)) {
            return false;
        }
        final Object this$type = this.type;
        final Object other$type = other.type;
        if (this$type == null ? other$type != null : !this$type.equals(other$type)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.name;
        result = result * PRIME + ($name == null ? 0 : $name.hashCode());
        final Object $query = this.query;
        result = result * PRIME + ($query == null ? 0 : $query.hashCode());
        final Object $overall_state = this.overall_state;
        result = result * PRIME + ($overall_state == null ? 0 : $overall_state.hashCode());
        final Object $type = this.type;
        result = result * PRIME + ($type == null ? 0 : $type.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof DataDogMonitor;
    }

    public String toString() {
        return "de.axelspringer.ideas.tools.dash.business.datadog.DataDogMonitor(name=" + this.name + ", query=" + this.query + ", overall_state=" + this.overall_state + ", type=" + this.type + ")";
    }
}
