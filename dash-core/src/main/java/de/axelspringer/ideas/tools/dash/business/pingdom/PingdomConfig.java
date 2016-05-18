package de.axelspringer.ideas.tools.dash.business.pingdom;

public class PingdomConfig {

    public final String userName;
    public final String password;
    public final String accountEmail;
    public final String appKey;

    public PingdomConfig(String userName, String password, String accountEmail, String appKey) {
        this.userName = userName;
        this.password = password;
        this.accountEmail = accountEmail;
        this.appKey = appKey;
    }
}
