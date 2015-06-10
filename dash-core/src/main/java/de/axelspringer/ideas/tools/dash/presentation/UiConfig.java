package de.axelspringer.ideas.tools.dash.presentation;

public class UiConfig {

    private final String title;

    public UiConfig(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UiConfig)) {
            return false;
        }
        final UiConfig other = (UiConfig) o;
        if (!other.canEqual((Object) this)) {
            return false;
        }
        final Object this$title = this.title;
        final Object other$title = other.title;
        if (this$title == null ? other$title != null : !this$title.equals(other$title)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $title = this.title;
        result = result * PRIME + ($title == null ? 0 : $title.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof UiConfig;
    }

    public String toString() {
        return "de.axelspringer.ideas.tools.dash.presentation.UiConfig(title=" + this.title + ")";
    }
}
