package com.frjgames.dal.models.exceptions;

/**
 * Exception thrown when a DB write fails a server-side conditional check.
 *
 * @author fridge
 */
public class ConditionalWriteException extends DataAccessLayerException {
    public ConditionalWriteException(String message) {
        super(message);
    }
    public ConditionalWriteException(String message, Throwable cause) {
        super(message, cause);
    }
    public ConditionalWriteException(Throwable cause) {
        super(cause);
    }
}
