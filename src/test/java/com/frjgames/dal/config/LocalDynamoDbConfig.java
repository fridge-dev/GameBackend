package com.frjgames.dal.config;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * Configure DynamoDb to use local, in-memory instance rather than remote instance.
 *
 * @author alecva
 */
public class LocalDynamoDbConfig extends DynamoDbConfig {

    @Getter(AccessLevel.PACKAGE)
    private final AmazonDynamoDB amazonDbLocal;

    public LocalDynamoDbConfig() {
        super(null, null);
        this.amazonDbLocal = DynamoDBEmbedded.create().amazonDynamoDB();
        super.setAmazonDynamoDB(this.amazonDbLocal);
    }
}