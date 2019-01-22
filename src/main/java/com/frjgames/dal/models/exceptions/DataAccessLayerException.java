package com.frjgames.dal.models.exceptions;

/**
 * TODO
 *
 * @author TODO
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
