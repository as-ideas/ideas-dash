package de.axelspringer.ideas.tools.dash.business.jenkins.joblist;

import de.axelspringer.ideas.tools.dash.business.jenkins.domain.JenkinsElement;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

public class JenkinsJobListCheckProviderJobNameMapper {

    public static String checkResultName(JenkinsElement job, String jobPrefix, boolean useUrl) {

        return Stream.of(job)
                .map(jobElement -> useUrl ? job.getUrl() : job.getName())
                .map(jobName -> jobName.contains("job/") ? jobName.substring(jobName.indexOf("job/") + 4).replaceAll("job/", "") : jobName)
                .map(jobName -> StringUtils.isBlank(jobPrefix) || !jobName.startsWith(jobPrefix) ? jobName : jobName.substring(jobPrefix.length()))
                .findFirst().orElseThrow(IllegalArgumentException::new);
    }
}
