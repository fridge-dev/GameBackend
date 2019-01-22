package com.frjgames.dal.ddb.accessors;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDeleteExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.IDynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.frjgames.dal.ddb.accessors.tablemgmt.FrjDynamoDbMapper;
import com.frjgames.dal.ddb.items.DdbItem;
import java.util.Optional;

/**
 * This class is responsible for adapting the {@link IDynamoDBMapper} from AWS SDK to the application.
 *
 * @author fridge
 */
public class DynamoDbAccessor<T extends DdbItem> {

    private final IDynamoDBMapper dynamoDbMapper;
    private final Class<T> clazz;

    public DynamoDbAccessor(final FrjDynamoDbMapper dynamoDbMapper, final Class<T> clazz) {
        this.dynamoDbMapper = dynamoDbMapper;
        this.clazz = clazz;

        dynamoDbMapper.assertTableExists(clazz);
    }

    /**
     * Load item from DynamoDB using hashKey
     */
    public Optional<T> loadItem(final Object hashKey) {
        return Optional.ofNullable(dynamoDbMapper.load(clazz, hashKey));
    }

    /**
     * Load item from DynamoDB using hashKey and rangeKey
     */
    public Optional<T> loadItem(final Object hashKey, final Object rangeKey) {
        return Optional.ofNullable(dynamoDbMapper.load(clazz, hashKey, rangeKey));
    }

    /**
     * Save item to DynamoDB
     */
    public void saveItem(final T item) {
        dynamoDbMapper.save(item);
    }

    /**
     * Save item to DynamoDB with an Expression
     */
    public void saveItem(final T item, final DynamoDBSaveExpression expression) {
        dynamoDbMapper.save(item, expression);
    }

    /**
     * Delete item from DynamoDB
     */
    public void deleteItem(final T item) {
        dynamoDbMapper.delete(item);
    }

    /**
     * Delete item from DynamoDB with an Expression
     */
    public void deleteItem(final T item, final DynamoDBDeleteExpression expression) {
        dynamoDbMapper.delete(item, expression);
    }

    /**
     * Query DynamoDB using the query expression and return a list that internally stores
     * pagination pointer so that it can automatically and lazily fetch the next page when
     * needed (e.g. when using an Iterator).
     */
    public PaginatedQueryList<T> queryAllItems(final DynamoDBQueryExpression<T> queryExpression) {
        return dynamoDbMapper.query(clazz, queryExpression);
    }

    /**
     * Query DynamoDB using the query expression and return a single page of DDB items and
     * expose the pagination pointer.
     */
    public QueryResultPage<T> querySinglePage(final DynamoDBQueryExpression<T> queryExpression) {
        return dynamoDbMapper.queryPage(clazz, queryExpression);
    }

}
