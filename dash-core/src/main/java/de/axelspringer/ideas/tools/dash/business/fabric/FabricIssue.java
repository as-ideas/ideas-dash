package de.axelspringer.ideas.tools.dash.business.fabric;

/**
 * DTO for fabric issue-endpoint
 */
public class FabricIssue {

    // brave assumption, lets see
    public final static int EVENT_TYPE_SEVERE = 1;

    private int crashes_count;

    private int notes_count;

    private int event_type;

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCrashes_count() {
        return crashes_count;
    }

    public void setCrashes_count(int crashes_count) {
        this.crashes_count = crashes_count;
    }

    public int getNotes_count() {
        return notes_count;
    }

    public void setNotes_count(int notes_count) {
        this.notes_count = notes_count;
    }

    public int getEvent_type() {
        return event_type;
    }

    public void setEvent_type(int event_type) {
        this.event_type = event_type;
    }
}
