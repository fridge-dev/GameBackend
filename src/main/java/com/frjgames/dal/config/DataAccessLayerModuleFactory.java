package com.frjgames.dal.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
import com.frjgames.dal.ddb.accessors.tablemgmt.DdbTableManager;
import com.frjgames.dal.ddb.accessors.tablemgmt.FrjDynamoDbMapper;
import com.frjgames.dal.ddb.items.DdbItem;
import java.util.Arrays;
import java.util.Collection;

/**
 * Factory for returning instances of {@link DataAccessLayerModule}.
 *
 * @author fridge
 */
public class DataAccessLayerModuleFactory {

    /**
     * Module for LIVE database.
     *
     * Wires together the {@link DynamoDbConfig} and {@link DataAccessConfig}.
     */
    public static DataAccessLayerModule getModule(final String awsRegion, final AWSCredentialsProvider awsCredentialsProvider) {
        DynamoDbConfig dynamoDbConfig = new DynamoDbConfig(awsRegion, awsCredentialsProvider);

        return new DataAccessConfig(dynamoDbConfig.dynamoDBMapper());
    }

    /**
     * Module for LOCAL database.
     *
     * Wires together the {@link DynamoDbConfig} and {@link DataAccessConfig}.
     */
    @SafeVarargs
    public static DataAccessLayerModule getModuleLocal(final Class<? extends DdbItem>... tablesToCreate) {
        return getModuleLocal(Arrays.asList(tablesToCreate));
    }

    /**
     * Module for LOCAL database.
     *
     * Wires together the {@link DynamoDbConfig} and {@link DataAccessConfig}.
     */
    public static DataAccessLayerModule getModuleLocal(final Collection<Class<? extends DdbItem>> tablesToCreate) {
        AmazonDynamoDB localDb = DynamoDBEmbedded.create().amazonDynamoDB();
        FrjDynamoDbMapper frjDynamoDbMapper = new DynamoDbConfig(localDb).dynamoDBMapper();

        DdbTableManager ddbTableManager = new DdbTableManager(localDb, frjDynamoDbMapper);

        for (Class<? extends DdbItem> table : tablesToCreate) {
            ddbTableManager.createTable(table, 5L, 5L);
        }

        return new DataAccessConfig(frjDynamoDbMapper);
    }

}
