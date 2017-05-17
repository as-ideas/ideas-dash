package de.axelspringer.ideas.tools.dash.business.datadog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

@JsonIgnoreProperties(ignoreUnknown = true, value = {"tags"})
public class DataDogMonitor {

    public final static String STATE_OK = "OK";
    public final static String STATE_NO_DATA = "No Data";
    public final static String STATE_ALERT = "Alert";

    // This is the inner data from the JSON
    private String name;
    private String query;
    @JsonProperty("overall_state")
    private String overallState;
    private String type;
    private Long id;
    private DataDogMonitorOptions options;
    @JsonProperty("matching_downtimes")
    private DataDogDowntime[] matchingDowntimes;

    // These are additional Infos
    private List<String> tags;

    public String getName() {
        return name;
    }

    public String getQuery() {
        return query;
    }

    public String getOverallState() {
        return overallState;
    }

    public Long getId() {
        return id;
    }

    public DataDogMonitorOptions getOptions() {
        return options;
    }

    public boolean isSilencedMonitor() {
        return !isActiveMonitor();
    }

    public boolean isActiveMonitor() {
        return options == null || !options.isSilenced();
    }

    public boolean isNotifyNoData() {
        return options != null && options.isNotifyNoData();
    }

    public boolean isOverallStateOk() {
        if (!isNotifyNoData() && STATE_NO_DATA.equals(overallState)) {
            // the overall state is ok if the state is "No Data" and the monitor is *not* set up to notify on no data
            return true;
        }
        return STATE_OK.equals(overallState);
    }

    public List<String> getTags() {
        if (tags == null) {
            tags = findAllTagsInQuery();
        }
        return tags;
    }

    public boolean hasTag(String tag) {
        return getTags().contains(tag);
    }

    public boolean hasAllTags(List<String> neededTags) {
        return getTags().containsAll(neededTags);
    }

    private List<String> findAllTagsInQuery() {
        List<String> tags = new ArrayList<>();
        Pattern p = Pattern.compile("\\{(.*?)\\}");
        Matcher m = p.matcher(query);

        while (m.find()) {
            final String group = m.group(1);
            final String[] split = group.split(",");
            for (String singleTag : split) {
                if (StringUtils.isNotBlank(singleTag)) {
                    tags.add(singleTag);
                }
            }
        }
        return tags;
    }

    public boolean hasActiveDowntime() {
        return matchingDowntimes == null ? false : Arrays.stream(matchingDowntimes).anyMatch(DataDogMonitor::isActiveDowntime);
    }

    private static boolean isActiveDowntime(DataDogDowntime dataDogDowntime) {
        return dataDogDowntime.active;
    }
}
