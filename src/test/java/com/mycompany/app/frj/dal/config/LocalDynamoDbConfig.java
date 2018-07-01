package com.mycompany.app.frj.dal.config;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
import lombok.Getter;

/**
 * TODO
 *
 * @author alecva
 */
public class LocalDynamoDbConfig extends DynamoDbConfig {

    @Getter
    private final AmazonDynamoDB amazonDbLocal;

    public LocalDynamoDbConfig() {
        super(null, null);
        this.amazonDbLocal = DynamoDBEmbedded.create().amazonDynamoDB();
        super.setAmazonDynamoDB(this.amazonDbLocal);
    }


}