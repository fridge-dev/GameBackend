package com.frjgames.dal.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.ConsistentReads;
import com.frjgames.dal.ddb.accessors.tablemgmt.FrjDynamoDbMapper;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;

/**
 * This config is responsible for configuring and creating the AWS DynamoDB client SDK instances.
 *
 * @author fridge
 */
@RequiredArgsConstructor // Constructor only meant to support DynamoDBLocal
/* NOT PUBLIC */ class DynamoDbConfig {

    /**
     * Dependencies
     */
    private final AmazonDynamoDB amazonDynamoDB;

    /**
     * Lazy singletons
     */
    private FrjDynamoDbMapper dynamoDBMapper;

    /**
     * Constructor for connecting to live DB.
     */
    public DynamoDbConfig(final String awsRegion, final AWSCredentialsProvider awsCredentials) {
        this.amazonDynamoDB = AmazonDynamoDBClient.builder()
                .withRegion(awsRegion)
                .withCredentials(awsCredentials)
                .build();
    }

    @Synchronized
    public FrjDynamoDbMapper dynamoDBMapper() {
        if (dynamoDBMapper == null) {
            dynamoDBMapper = new FrjDynamoDbMapperImpl(this.amazonDynamoDB, getDdbClientConfig());
        }

        return dynamoDBMapper;
    }

    private DynamoDBMapperConfig getDdbClientConfig() {
        return DynamoDBMapperConfig.builder()
                .withConsistentReads(ConsistentReads.EVENTUAL)
                .build();
    }
}
