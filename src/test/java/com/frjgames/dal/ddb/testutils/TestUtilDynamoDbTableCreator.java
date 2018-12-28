package com.frjgames.dal.ddb.testutils;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.CreateTableResult;
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.LocalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;

/**
 * Test utility for creating local dynamo DB tables.
 *
 * @author alecva
 */
public final class TestUtilDynamoDbTableCreator {

    private static final long DEFAULT_RCU = 5L;
    private static final long DEFAULT_WCU = 5L;

    /**
     * Creates a table with the given DDB Item.
     */
    public static CreateTableResult createTable(final AmazonDynamoDB dynamoDb, final DynamoDBMapper dynamoDbMapper, final Class<?> ddbItemClazz) {
        return createTable(dynamoDb, dynamoDbMapper, ddbItemClazz, DEFAULT_RCU, DEFAULT_WCU);
    }

    private static CreateTableResult createTable(
            final AmazonDynamoDB dynamoDb,
            final DynamoDBMapper dynamoDbMapper,
            final Class<?> clazz,
            final long rcu,
            final long wcu
    ) {
        CreateTableRequest tableRequest = dynamoDbMapper.generateCreateTableRequest(clazz).withProvisionedThroughput(new ProvisionedThroughput(rcu, wcu));

        deleteTableIfExists(dynamoDb, tableRequest.getTableName());

        setGsiCapacityAndProjection(tableRequest.getGlobalSecondaryIndexes(), rcu, wcu);
        setLsiProjection(tableRequest.getLocalSecondaryIndexes());

        return dynamoDb.createTable(tableRequest);
    }

    private static void deleteTableIfExists(final AmazonDynamoDB dynamoDb, final String tableName) {
        try {
            dynamoDb.deleteTable(tableName);
        } catch (ResourceNotFoundException e) {
            // Expected on the first test of the suite.
        }
    }

    private static void setGsiCapacityAndProjection(final List<GlobalSecondaryIndex> globalSecondaryIndexes, final long rcu, final long wcu) {
        if (CollectionUtils.isEmpty(globalSecondaryIndexes)) {
            return;
        }

        for (GlobalSecondaryIndex globalSecondaryIndex : globalSecondaryIndexes) {
            globalSecondaryIndex.setProvisionedThroughput(new ProvisionedThroughput(rcu, wcu));
            globalSecondaryIndex.setProjection(new Projection().withProjectionType(ProjectionType.ALL));
        }
    }

    private static void setLsiProjection(final List<LocalSecondaryIndex> localSecondaryIndexes) {
        if (CollectionUtils.isEmpty(localSecondaryIndexes)) {
            return;
        }

        for (LocalSecondaryIndex localSecondaryIndex : localSecondaryIndexes) {
            localSecondaryIndex.setProjection(new Projection().withProjectionType(ProjectionType.ALL));
        }
    }
}
