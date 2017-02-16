package de.axelspringer.ideas.tools.dash.business.jenkins.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JenkinsElement {

    @JsonProperty("_class")
    private String elementType;

    private String name;

    private String url;

    private String color;

    public String getElementType() {
        return elementType;
    }

    public void setElementType(String elementType) {
        this.elementType = elementType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
