package de.axelspringer.ideas.tools.dash.business.gocd;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GoCdPagination {

    public Long offset;
    public Long total;
    public Long page_size;
}
