package com.frjgames.dal.config;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.frjgames.dal.ddb.items.DdbItem;
import com.frjgames.dal.ddb.accessors.tablemgmt.FrjDynamoDbMapper;
import com.frjgames.dal.ddb.accessors.tablemgmt.DdbTableManager;

/**
 * Custom implementation of the interface, which delegates all method calls to either
 * {@link DynamoDBMapper} or {@link DdbTableManager}.
 *
 * @author fridge
 */
/* NOT PUBLIC */ class FrjDynamoDbMapperImpl extends DynamoDBMapper implements FrjDynamoDbMapper {

    private final DdbTableManager ddbTableManager;

    public FrjDynamoDbMapperImpl(final AmazonDynamoDB dynamoDB, final DynamoDBMapperConfig config) {
        super(dynamoDB, config);
        this.ddbTableManager = new DdbTableManager(dynamoDB, this);
    }

    @Override
    public <T extends DdbItem> void assertTableExists(final Class<T> clazz) {
        this.ddbTableManager.assertTableExists(clazz);
    }

}
