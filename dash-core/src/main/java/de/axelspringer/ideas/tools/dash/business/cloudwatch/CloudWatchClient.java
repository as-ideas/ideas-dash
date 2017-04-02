package de.axelspringer.ideas.tools.dash.business.cloudwatch;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import com.amazonaws.services.cloudwatch.model.DescribeAlarmsResult;
import org.springframework.stereotype.Service;

@Service
public class CloudWatchClient {

    public DescribeAlarmsResult describeAlarms(String awsAccessKeyId, String awsSecretKey) {

        final AWSCredentials credentials = new BasicAWSCredentials(awsAccessKeyId, awsSecretKey);
        final AWSCredentialsProvider credentialProvider = new AWSStaticCredentialsProvider(credentials);
        final AmazonCloudWatchClientBuilder clientBuilder = AmazonCloudWatchClientBuilder
                .standard();
        clientBuilder.setCredentials(credentialProvider);
        final AmazonCloudWatch cloudwatch = clientBuilder.build();
        return cloudwatch.describeAlarms();
    }
}
