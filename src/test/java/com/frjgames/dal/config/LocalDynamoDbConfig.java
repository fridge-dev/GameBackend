package com.frjgames.dal.config;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
import lombok.Getter;

/**
 * Configure DynamoDb to use local, in-memory instance rather than remote instance.
 *
 * Extends the existing {@link DynamoDbConfig} class to re-use as much configuration from the src code as possible,
 * only overriding the AmazonDynamoDB instance.
 *
 * @author alecva
 */
public class LocalDynamoDbConfig extends DynamoDbConfig {

    @Getter
    private final AmazonDynamoDB amazonDbLocal = DynamoDBEmbedded.create().amazonDynamoDB();

    public LocalDynamoDbConfig() {
        super(null, null);
        super.setAmazonDynamoDB(this.amazonDbLocal);
    }
}