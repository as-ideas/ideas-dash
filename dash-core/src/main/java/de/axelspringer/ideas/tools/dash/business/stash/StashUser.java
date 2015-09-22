package de.axelspringer.ideas.tools.dash.business.stash;

public class StashUser {

    private final String name;
    private final String nameDisplayed;

    public StashUser(String name, String nameDisplayed) {
        this.name = name;
        this.nameDisplayed = nameDisplayed;
    }

    public String name() {
        return name;
    }

    public String nameDisplayed() {
        return nameDisplayed;
    }
}
