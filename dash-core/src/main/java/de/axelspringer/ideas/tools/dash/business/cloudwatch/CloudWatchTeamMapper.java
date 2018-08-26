package de.axelspringer.ideas.tools.dash.business.cloudwatch;

import com.amazonaws.services.cloudwatch.model.MetricAlarm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Component
public class CloudWatchTeamMapper {

    @Autowired
    private CloudwatchProperties properties;

    private ConcurrentHashMap<String, Pattern> patternCache = new ConcurrentHashMap<>();

    public List<String> map(MetricAlarm metricAlarm) {
        if (properties.getAlarmToTeamsMapping() == null) {
            return emptyList();
        }

        String alarmName = metricAlarm.getAlarmName();

        return properties.getAlarmToTeamsMapping().entrySet().stream()
                .flatMap(entry -> mapToTeam(entry, alarmName))
                .distinct()
                .collect(toList());
    }

    private Stream<String> mapToTeam(Map.Entry<String, List<String>> regexesForTeam, String alarmName) {
        String team = regexesForTeam.getKey();
        List<String> regexes = regexesForTeam.getValue();

        boolean matches = regexes.stream().anyMatch(regex -> {
            Pattern pattern = compileOrGetCachedPattern(regex);

            return pattern.matcher(alarmName).matches();
        });

        if (matches) {
            return Stream.of(team);
        } else {
            return Stream.empty();
        }
    }

    private Pattern compileOrGetCachedPattern(String regex) {
        return patternCache.computeIfAbsent(regex, Pattern::compile);
    }
}
