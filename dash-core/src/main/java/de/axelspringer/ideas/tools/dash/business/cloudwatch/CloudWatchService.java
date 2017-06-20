package de.axelspringer.ideas.tools.dash.business.cloudwatch;

import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import com.amazonaws.services.cloudwatch.model.DescribeAlarmsResult;
import org.springframework.stereotype.Service;

@Service
public class CloudWatchService {

    public DescribeAlarmsResult describeAlarms(String awsRegion) {

        return AmazonCloudWatchClientBuilder
                .standard()
                .withRegion(awsRegion)
                .build()
                .describeAlarms();
    }
}
