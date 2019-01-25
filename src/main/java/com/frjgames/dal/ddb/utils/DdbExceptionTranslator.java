package com.frjgames.dal.ddb.utils;

import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.frjgames.dal.models.exceptions.ConditionalWriteException;
import lombok.experimental.UtilityClass;

/**
 * This class is responsible for translating DynamoDB SDK exceptions to our custom DAL layer exceptions.
 *
 * @author fridge
 */
@UtilityClass
public class DdbExceptionTranslator {

    /**
     * Wraps any DDB write using a conditional check.
     */
    public static void conditionalWrite(final Runnable runnable, final String exceptionMessage) {
        try {
            runnable.run();
        } catch (ConditionalCheckFailedException e) {
            throw new ConditionalWriteException(exceptionMessage, e);
        }
    }

}
