package com.mycompany.app.frj.dal.config.ddb;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;

/**
 * TODO
 *
 * @author alecva
 */
public class LocalDynamoDbConfig extends DynamoDbConfig {

    public LocalDynamoDbConfig() {
        super(null, null);
    }

    @Override
    protected AmazonDynamoDB amazonDynamoDB() {
        return DynamoDBEmbedded.create().amazonDynamoDB();
    }
}
