package com.mycompany.app.frj.dal.config.ddb;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.ConsistentReads;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;

/**
 * TODO
 *
 * @author alecva
 */
@RequiredArgsConstructor
public class DynamoDbConfig {

    private final String awsRegion;

    private final String awsKey;

    /**
     * Lazy singletons
     */
    private DynamoDBMapper dynamoDbMapper;

    @Synchronized
    public DynamoDBMapper dynamoDBMapper() {
        if (dynamoDbMapper == null) {
            dynamoDbMapper = instantiateDynamoDbMapper();
        }

        return dynamoDbMapper;
    }

    private DynamoDBMapper instantiateDynamoDbMapper() {
        return new DynamoDBMapper(amazonDynamoDB(), getDdbClientConfig());
    }

    protected AmazonDynamoDB amazonDynamoDB() {
        return AmazonDynamoDBClient.builder()
                .withRegion(awsRegion)
                .withCredentials(getAwsCredentials())
                .build();
    }

    // Consider moving this to generic AWS config if onboarded with more services.
    private AWSCredentialsProvider getAwsCredentials() {
        // TODO use #awsKey somehow.
        return null;
    }

    private DynamoDBMapperConfig getDdbClientConfig() {
        return DynamoDBMapperConfig.builder()
                .withConsistentReads(ConsistentReads.EVENTUAL)
                .build();
    }

}
