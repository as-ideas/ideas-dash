package de.axelspringer.ideas.tools.dash.business.pingdom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PingdomAnswer {

    public Long id;
    public Long created;
    public String name;
    public String hostname;
    public Boolean use_legacy_notifications;
    public Long resolution;
    public String type;
    public Boolean ipv6;
    public Long lasterrortime;
    public Long lasttesttime;
    public Long lastresponsetime;
    public String status;
    public Long alert_policy;
    public String alert_policy_name;
    public Long acktimeout;
    public Long autoresolve;

}
