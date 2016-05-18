package de.axelspringer.ideas.tools.dash.business.gocd;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GoCdPipeline {
    public String label;
    public String name;
    public Long natural_order;
    public Boolean can_run;
    public List<GoCdStage> stages;

    public Boolean preparing_to_schedule;
    public Long counter;
    public String comment;

}
