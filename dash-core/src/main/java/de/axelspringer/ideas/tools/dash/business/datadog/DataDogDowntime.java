package de.axelspringer.ideas.tools.dash.business.datadog;

import java.util.Date;
import java.util.List;

//    {
//        "disabled": false,
//            "start": 1437384240,
//            "end": 1437386400,
//            "parent_id": null,
//            "recurrence": null,
//            "canceled": null,
//            "active": true,
//            "scope": [
//        "pcp-jenkins"
//        ],
//        "message": "Test from Sebastian Waschnick",
//            "id": 90983227
//    }
public class DataDogDowntime {
    public Long id;
    public Boolean disabled;
    public Date start;
    public Date end;
    public Long parent_id;
    public String recurrence;
    public Boolean canceled;
    public Boolean active;
    public List<String> scope;
    public String message;

}