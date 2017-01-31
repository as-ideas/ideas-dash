package de.axelspringer.ideas.tools.dash.presentation;

public enum State{

    GREY,
    GREEN,
    YELLOW,
    RED;


    @Override
    public String toString() {
        return name().toLowerCase();
    }

}
