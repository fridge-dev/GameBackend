package com.frjgames.dal.ddb.accessors.tablemgmt;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.IDynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.CreateTableResult;
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.LocalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.amazonaws.services.dynamodbv2.model.TableStatus;
import com.frjgames.dal.ddb.items.DdbItem;
import com.google.common.base.Preconditions;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

/**
 * This class is responsible for managing table status of various tables in DynamoDB.
 *
 * @author fridge
 */
@RequiredArgsConstructor
public class DdbTableManager {

    private final AmazonDynamoDB dynamoDb;
    private final IDynamoDBMapper dynamoDbMapper;

    /**
     * @throws IllegalStateException if the table for the class doesn't exist.
     */
    public <T extends DdbItem> void assertTableExists(final Class<T> clazz) {
        String tableName = dynamoDbMapper.generateCreateTableRequest(clazz).getTableName();
        Preconditions.checkState(doesTableExist(tableName), "Expected DynamoDB table %s to exist.", tableName);
    }

    private boolean doesTableExist(final String tableName) {
        try {
            String tableStatus = dynamoDb.describeTable(tableName)
                    .getTable()
                    .getTableStatus();

            return TableStatus.ACTIVE.toString().equals(tableStatus);
        } catch (ResourceNotFoundException e) {
            return false;
        }
    }

    /**
     * Creates a new table in DynamoDB.
     */
    public <T extends DdbItem> CreateTableResult createTable(final Class<T> clazz, final long rcu, final long wcu) {
        CreateTableRequest tableRequest = dynamoDbMapper.generateCreateTableRequest(clazz).withProvisionedThroughput(new ProvisionedThroughput(rcu, wcu));
        setGsiCapacityAndProjection(tableRequest.getGlobalSecondaryIndexes(), rcu, wcu);
        setLsiProjection(tableRequest.getLocalSecondaryIndexes());

        String tableName = tableRequest.getTableName();
        Preconditions.checkState(!doesTableExist(tableName), "About to create table %s in DynamoDB, but it already exists.", tableName);

        return dynamoDb.createTable(tableRequest);
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
