package de.axelspringer.ideas.tools.dash.business.check;

public class Host {

    private String nameIp;

    public Host(String nameIp) {
        this.nameIp = nameIp;
    }

    public String getNameIp() {
        return this.nameIp;
    }

    public void setNameIp(String nameIp) {
        this.nameIp = nameIp;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Host)) {
            return false;
        }
        final Host other = (Host) o;
        if (!other.canEqual((Object) this)) {
            return false;
        }
        final Object this$nameIp = this.nameIp;
        final Object other$nameIp = other.nameIp;
        if (this$nameIp == null ? other$nameIp != null : !this$nameIp.equals(other$nameIp)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $nameIp = this.nameIp;
        result = result * PRIME + ($nameIp == null ? 0 : $nameIp.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Host;
    }

    public String toString() {
        return "de.axelspringer.ideas.tools.dash.business.check.Host(nameIp=" + this.nameIp + ")";
    }
}
