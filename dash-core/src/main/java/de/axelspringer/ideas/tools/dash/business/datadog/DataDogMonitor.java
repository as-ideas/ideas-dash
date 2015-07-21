package de.axelspringer.ideas.tools.dash.business.datadog;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataDogMonitor {

    public final static String STATE_OK = "OK";

    // This is the inner data from the JSON
    private String name;
    private String query;
    private String overall_state;
    private String type;
    private DataDogMonitorOptions options;

    // These are additional Infos
    private List<String> tags;
    private MonitorState state;

    public String getName() {
        return name;
    }

    public String getQuery() {
        return query;
    }

    public String getOverallState() {
        return overall_state;
    }

    public boolean isSilencedMonitor() {
        return !isActiveMonitor();
    }

    public boolean isActiveMonitor() {
        return options == null || !options.isSilenced();
    }

    public boolean isOverallStateOk() {
        return STATE_OK.equals(overall_state);
    }

    public List<String> getTags() {
        if (tags == null) {
            tags = findAllTagsInQuery();
        }
        return tags;
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


}
