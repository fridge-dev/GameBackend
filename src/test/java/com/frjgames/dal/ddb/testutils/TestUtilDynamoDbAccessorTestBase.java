package com.frjgames.dal.ddb.testutils;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.frjgames.dal.config.LocalDynamoDbConfig;
import com.frjgames.dal.ddb.accessors.BaseDynamoDbAccessor;
import com.frjgames.dal.ddb.items.DdbItem;
import java.util.function.Function;
import org.junit.Before;

/**
 * Test utility that offers a small DRY benefit for accessors following frj pattern.
 *
 * @author alecva
 */
public abstract class TestUtilDynamoDbAccessorTestBase<T extends DdbItem, A extends BaseDynamoDbAccessor<T>> {

    private final LocalDynamoDbConfig localDynamoDbConfig = new LocalDynamoDbConfig();

    private final Function<DynamoDBMapper, A> accessorConstructor;
    private final Class<T> itemType;

    protected A accessor;

    public TestUtilDynamoDbAccessorTestBase(final Class<T> itemType, final Function<DynamoDBMapper, A> accessorConstructor) {
        this.itemType = itemType;
        this.accessorConstructor = accessorConstructor;
    }

    @Before
    public void superBefore() throws Exception {
        createTable();
        constructAccessor();
    }

    /**
     * Call this in the @Before method to create your table.
     *
     * Note: This is intentionally made non-static, because it forces each test to start with an empty DB,
     *       which has no chance of a test relying on data present in the table from a previous test.
     */
    private void createTable() throws Exception {
        TestUtilDynamoDbTableCreator.createTable(localDynamoDbConfig.getAmazonDbLocal(), localDynamoDbConfig.dynamoDBMapper(), itemType);
    }

    private void constructAccessor() {
        accessor = accessorConstructor.apply(localDynamoDbConfig.dynamoDBMapper());
    }
}
