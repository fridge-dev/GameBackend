package com.frjgames.dal.ddb.accessors.tablemgmt;

import com.amazonaws.services.dynamodbv2.datamodeling.IDynamoDBMapper;
import com.frjgames.dal.ddb.items.DdbItem;

/**
 * This interface exists so that the {@link com.frjgames.dal.ddb.accessors.DynamoDbAccessor} can validate the underlying table
 * exists in DynamoDB. We use this interface in place of IDynamoDBMapper because we can avoid passing in a
 * {@link com.amazonaws.services.dynamodbv2.AmazonDynamoDB} to the constructor.
 *
 * @author fridge
 */
public interface FrjDynamoDbMapper extends IDynamoDBMapper {

    <T extends DdbItem> void assertTableExists(final Class<T> clazz);

}
