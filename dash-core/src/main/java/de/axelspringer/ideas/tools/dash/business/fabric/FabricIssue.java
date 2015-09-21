package de.axelspringer.ideas.tools.dash.business.fabric;

/**
 * DTO for fabric issue-endpoint
 */
public class FabricIssue {

    private int crashes_count;

    private int notes_count;

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
}
