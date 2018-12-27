package com.frjgames.dal.ddb.utils;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDeleteExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import lombok.experimental.UtilityClass;

/**
 * This class contains utilities related to building {@link DynamoDBSaveExpression} or {@link DynamoDBDeleteExpression} objects.
 *
 * @author fridge
 */
@UtilityClass
public class DdbExpressionFactory {

    /**
     * Make a DynamoDB save expression with the condition that the provided column doesn't exist.
     *
     * Call with your table's hash key as the columnName and this conditional will be for the item not existing.
     */
    public static DynamoDBSaveExpression newSaveExpressionItemDoesntExist(final String columnName) {
        Map<String, ExpectedAttributeValue> expectUserDoesntExist = ImmutableMap.of(columnName, new ExpectedAttributeValue(false));

        return new DynamoDBSaveExpression().withExpected(expectUserDoesntExist);
    }

}
