package de.axelspringer.ideas.tools.dash.business.cloudwatch;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import com.amazonaws.services.cloudwatch.model.DescribeAlarmsResult;
import org.springframework.stereotype.Service;

@Service
public class CloudWatchService {

    public DescribeAlarmsResult describeAlarms(String awsRegion) {

        final DefaultAWSCredentialsProviderChain credentialsProviderChain = new DefaultAWSCredentialsProviderChain();
        final AWSStaticCredentialsProvider credentialProvider = new AWSStaticCredentialsProvider(credentialsProviderChain.getCredentials());
        return AmazonCloudWatchClientBuilder
                .standard()
                .withCredentials(credentialProvider)
                .withRegion(awsRegion)
                .build()
                .describeAlarms();
    }
}
