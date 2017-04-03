package de.axelspringer.ideas.tools.dash.business.cloudwatch;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import com.amazonaws.services.cloudwatch.model.DescribeAlarmsResult;
import org.springframework.stereotype.Service;

@Service
public class CloudWatchService {

    public DescribeAlarmsResult describeAlarms(String awsAccessKeyId, String awsSecretKey, String awsRegion) {

        final AWSCredentials credentials = new BasicAWSCredentials(awsAccessKeyId, awsSecretKey);
        final AWSCredentialsProvider credentialProvider = new AWSStaticCredentialsProvider(credentials);
        return AmazonCloudWatchClientBuilder
                .standard()
                .withCredentials(credentialProvider)
                .withRegion(awsRegion)
                .build()
                .describeAlarms();
    }
}
