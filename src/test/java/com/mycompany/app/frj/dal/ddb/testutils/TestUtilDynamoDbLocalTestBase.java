package com.mycompany.app.frj.dal.ddb.testutils;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
import lombok.RequiredArgsConstructor;
import org.junit.Before;

/**
 * A class that provides utility methods for creating DynamoDB tables and testing DynamoDB
 * accessors locally, using the DynamoDBLocal library.
 *
 * @author alecva
 */
@RequiredArgsConstructor
public abstract class TestUtilDynamoDbLocalTestBase<T> {

    /**
     * Protected so sub-classes can use DynamoDBMapper for constructing the accessor.
     */
    private final AmazonDynamoDB dynamoDb = DynamoDBEmbedded.create().amazonDynamoDB();
    protected final DynamoDBMapper dynamoDbMapper = new DynamoDBMapper(dynamoDb);

    private final Class<T> itemType;

    /**
     * Call this in the @Before method to create your table.
     *
     * Note: This is intentionally made non-static, because it forces each test to start with an empty DB,
     *       which has no chance of a test relying on data present in the table from a previous test.
     */
    @Before
    public void createTable() throws Exception {
        TestUtilDynamoDbTableCreator.createTable(dynamoDb, dynamoDbMapper, itemType);
    }

}
