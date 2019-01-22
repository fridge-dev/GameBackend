package com.frjgames.dal.models.exceptions;

/**
 * Base exception class to throw when something bad happens in the DAL.
 *
 * @author fridge
 */
public class DataAccessLayerException extends RuntimeException {

    public DataAccessLayerException(final String message) {
        super(message);
    }
    public DataAccessLayerException(final String message, final Throwable cause) {
        super(message, cause);
    }
    public DataAccessLayerException(final Throwable cause) {
        super(cause);
    }

}
