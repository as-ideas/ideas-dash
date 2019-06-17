package de.axelspringer.ideas.tools.dash.business.jira.rest;

import com.google.gson.JsonElement;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.TimeZone;

public class Fields {
    /**
     * JIRA has a non standard ISO Date format E.G. 2016-01-18T17:16:59.000+0100 - https://answers.atlassian.com/questions/180275/update-jira-rest-api-datetime-value
     */
    private static final String JIRA_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm";

    /**
     * Team
     * @deprecated Use {@link #all} instead
     */
    private CustomField customfield_10144;

    /**
     * Team
     * @deprecated Use {@link #all} instead
     */
    private CustomField customfield_11400;

    private Assignee assignee;

    private Priority priority;

    private IssueType issuetype;

    private IssueStatus status;

    private String created;

    private String summary;

    /**
     * This is not an actual field returned from jira, but a collection of all the fields.
     *
     * Useful for accessing custom fields like "customfield_10500" for which there is no mapping here
     */
    private Map<String, JsonElement> all;

    public Fields() {
    }

    public String getCreated() {
        return created;
    }

    public LocalDateTime getCreatedAtDateTime() {
        //Note this is not precise but mostly good enough - captures the essential fields up to hh:mm
        //Decided not to map this at Gson Level as it may have undesireable side effects for other date fields with different date formats.
        DateFormat format = new SimpleDateFormat(JIRA_DATE_FORMAT);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return LocalDateTime.ofInstant(format.parse(getCreated()).toInstant(), ZoneId.systemDefault());
        } catch (ParseException e) {
            throw new RuntimeException("Failed to parse issue creation date " + getCreated(), e);
        }
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public boolean isBug() {
        return issuetype.getName().equalsIgnoreCase("Bug");
    }

    public CustomField getCustomfield_10144() {
        return this.customfield_10144;
    }

    public void setCustomfield_10144(CustomField customfield_10144) {
        this.customfield_10144 = customfield_10144;
    }

    public CustomField getCustomfield_11400() {
        return customfield_11400;
    }

    public void setCustomfield_11400(CustomField customfield_11400) {
        this.customfield_11400 = customfield_11400;
    }

    public Assignee getAssignee() {
        return this.assignee;
    }

    public void setAssignee(Assignee assignee) {
        this.assignee = assignee;
    }

    public Priority getPriority() {
        return this.priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public IssueType getIssuetype() {
        return this.issuetype;
    }

    public void setIssuetype(IssueType issuetype) {
        this.issuetype = issuetype;
    }

    public IssueStatus getStatus() {
        return this.status;
    }

    public void setStatus(IssueStatus status) {
        this.status = status;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Map<String, JsonElement> getAll() {
        return all;
    }

    public void setAll(Map<String, JsonElement> all) {
        this.all = all;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
