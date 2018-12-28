package com.frjgames.dal.config;

import com.frjgames.dal.ddb.items.DdbItem;
import com.frjgames.dal.ddb.testutils.TestUtilDynamoDbTableCreator;
import java.util.Arrays;
import java.util.List;

/**
 * Data Access configuration which utilizes the local DynamoDB configuration.
 *
 * @author alecva
 */
public class TestDataAccessConfig extends DataAccessConfig {

    private final LocalDynamoDbConfig localDdbConfig = new LocalDynamoDbConfig();

    @SafeVarargs
    public TestDataAccessConfig(final Class<? extends DdbItem>... tablesToCreate) {
        this(Arrays.asList(tablesToCreate));
    }

    public TestDataAccessConfig(final List<Class<? extends DdbItem>> tablesToCreate) {
        super(null, null);

        super.setDynamoDbConfig(localDdbConfig);

        for (Class<? extends DdbItem> clazz : tablesToCreate) {
            createTable(clazz);
        }
    }

    private void createTable(final Class<? extends DdbItem> clazz) {
        TestUtilDynamoDbTableCreator.createTable(localDdbConfig.getAmazonDbLocal(), localDdbConfig.dynamoDBMapper(), clazz);
    }

}
