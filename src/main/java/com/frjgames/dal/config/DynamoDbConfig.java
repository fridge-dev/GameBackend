package com.frjgames.dal.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.ConsistentReads;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.Synchronized;

/**
 * This config is responsible for creating the AWS DynamoDB client instance.
 *
 * @author fridge
 */
@RequiredArgsConstructor
/* pkg */ class DynamoDbConfig {

    /**
     * Dependencies
     */
    private final String awsRegion;
    private final String awsKey;

    /**
     * Lazy singletons
     */
    private DynamoDBMapper dynamoDBMapper;

    @Setter(AccessLevel.PACKAGE)
    private AmazonDynamoDB amazonDynamoDB;

    /**
     * If this isn't lazily created, then UTs cannot override the Local DB via Setter.
     */
    @Synchronized
    public DynamoDBMapper dynamoDBMapper() {
        if (dynamoDBMapper == null) {
            dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB(), getDdbClientConfig());
        }

        return dynamoDBMapper;
    }

    private AmazonDynamoDB amazonDynamoDB() {
        if (amazonDynamoDB == null) {
            amazonDynamoDB = AmazonDynamoDBClient.builder()
                    .withRegion(awsRegion)
                    .withCredentials(getAwsCredentials())
                    .build();
        }

        return amazonDynamoDB;
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
