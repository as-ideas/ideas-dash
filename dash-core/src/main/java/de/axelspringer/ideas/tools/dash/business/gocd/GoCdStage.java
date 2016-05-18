package de.axelspringer.ideas.tools.dash.business.gocd;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GoCdStage {

    public String result;
    public List<GoCdJob> jobs;
    public String name;
    public String rerun_of_counter;
    public String approval_type;
    public Boolean scheduled;
    public Boolean operate_permission;
    public String approved_by;
    public Boolean can_run;
    public Long id;
    public Long counter;
}
