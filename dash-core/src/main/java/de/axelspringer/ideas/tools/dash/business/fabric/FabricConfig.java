package de.axelspringer.ideas.tools.dash.business.fabric;

/**
 * DTO for fabric config endpoint
 */
public class FabricConfig {

    private String developer_token;

    public FabricConfig() {
    }

    public String getDeveloper_token() {
        return this.developer_token;
    }

    public void setDeveloper_token(String developer_token) {
        this.developer_token = developer_token;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FabricConfig)) {
            return false;
        }
        final FabricConfig other = (FabricConfig) o;
        if (!other.canEqual((Object) this)) {
            return false;
        }
        final Object this$developer_token = this.developer_token;
        final Object other$developer_token = other.developer_token;
        if (this$developer_token == null ? other$developer_token != null : !this$developer_token.equals(other$developer_token)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $developer_token = this.developer_token;
        result = result * PRIME + ($developer_token == null ? 0 : $developer_token.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof FabricConfig;
    }

    public String toString() {
        return "de.axelspringer.ideas.tools.dash.business.fabric.FabricConfig(developer_token=" + this.developer_token + ")";
    }
}
