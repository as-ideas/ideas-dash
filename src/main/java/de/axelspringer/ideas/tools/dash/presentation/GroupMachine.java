package de.axelspringer.ideas.tools.dash.presentation;

public enum GroupMachine {
    SSO("sso"), PWL("pwl"), BACKEND("backend");

    /* never ever changing id*/
    private String id;

    private GroupMachine(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        switch (this) {
            case SSO:
                return "SSO";
            case PWL:
                return "PWL";
            case BACKEND:
                return "BE";
        }

        throw new IllegalStateException("The group machine has no textual representation");
    }
}
