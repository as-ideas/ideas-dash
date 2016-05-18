package de.axelspringer.ideas.tools.dash.business.gocd;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GoCdJob {
    public String state;
    public String result;
    public String name;
    public Long id;
    public Long scheduled_date;
}
