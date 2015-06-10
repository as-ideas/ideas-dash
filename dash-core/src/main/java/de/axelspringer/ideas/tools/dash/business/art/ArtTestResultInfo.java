package de.axelspringer.ideas.tools.dash.business.art;

/**
 * @author Timo Ulich
 */
public class ArtTestResultInfo {

    private String status;

    public ArtTestResultInfo() {
    }

    public boolean isSuccessfull() {
        return "success".equalsIgnoreCase(status);
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ArtTestResultInfo)) {
            return false;
        }
        final ArtTestResultInfo other = (ArtTestResultInfo) o;
        if (!other.canEqual((Object) this)) {
            return false;
        }
        final Object this$status = this.status;
        final Object other$status = other.status;
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $status = this.status;
        result = result * PRIME + ($status == null ? 0 : $status.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ArtTestResultInfo;
    }

    public String toString() {
        return "de.axelspringer.ideas.tools.dash.business.art.ArtTestResultInfo(status=" + this.status + ")";
    }
}
