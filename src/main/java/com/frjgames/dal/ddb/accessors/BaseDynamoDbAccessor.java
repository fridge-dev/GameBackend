package com.frjgames.dal.ddb.accessors;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDeleteExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.frjgames.dal.ddb.items.DdbItem;
import java.util.Optional;
import lombok.AllArgsConstructor;

/**
 * Base class that implements the basic dynamo load, query, save operations for all derived classes to use.
 *
 * @author fridge
 */
@AllArgsConstructor
public abstract class BaseDynamoDbAccessor<T extends DdbItem> {

    private DynamoDBMapper dbMapper;
    private Class<T> clazz;

    /**
     * Load item from DynamoDB using key
     */
    protected Optional<T> loadItem(final Object key) {
        return Optional.ofNullable(dbMapper.load(clazz, key));
    }

    /**
     * Load item from DynamoDB using key and range
     */
    protected Optional<T> loadItem(final Object key, final Object range) {
        return Optional.ofNullable(dbMapper.load(clazz, key, range));
    }

    /**
     * Save item to DynamoDB
     */
    protected void saveItem(final T item) {
        dbMapper.save(item);
    }

    /**
     * Save item to DynamoDB with an Expression
     */
    protected void saveItem(T item, DynamoDBSaveExpression expression) {
        dbMapper.save(item, expression);
    }

    /**
     * Delete item from DynamoDB
     */
    protected void deleteItem(final T item) {
        dbMapper.delete(item);
    }

    /**
     * Delete item from DynamoDB with an Expression
     */
    protected void deleteItem(final T item, final DynamoDBDeleteExpression expression) {
        dbMapper.delete(item, expression);
    }

    /**
     * Query DynamoDB using the query expression and return a list that internally stores
     * pagination pointer so that it can automatically and lazily fetch the next page when
     * needed (e.g. when using an Iterator).
     */
    protected PaginatedQueryList<T> queryAllItems(final DynamoDBQueryExpression<T> queryExpression) {
        return dbMapper.query(clazz, queryExpression);
    }

    /**
     * Query DynamoDB using the query expression and return a single page of DDB items and
     * expose the pagination pointer.
     */
    protected QueryResultPage<T> querySinglePage(final DynamoDBQueryExpression<T> queryExpression) {
        return dbMapper.queryPage(clazz, queryExpression);
    }

}
