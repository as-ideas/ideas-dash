package de.axelspringer.ideas.tools.dash.business.datadog;

import java.util.Date;
import java.util.List;
//
//      {
//        "end": 1495286160,
//        "monitor_id": 402769,
//        "start": 1494249360,
//        "groups": [],
//        "active": true,
//        "scope": [
//          "pcp-crypto"
//        ],
//        "id": 225461549
//      }

public class DataDogDowntime {
    public Long id;
    public Long monitor_id;
    public Date start;
    public Date end;
    public Boolean active;
    public List<String> scope;
    public List<String> groups;
}
