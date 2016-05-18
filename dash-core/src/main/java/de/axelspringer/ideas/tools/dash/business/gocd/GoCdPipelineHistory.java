package de.axelspringer.ideas.tools.dash.business.gocd;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GoCdPipelineHistory {

    public GoCdPagination pagination;
    public List<GoCdPipeline> pipelines;
}
