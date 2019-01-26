package com.frjgames.dal.models.exceptions;

/**
 * This is a DAL exception to be thrown when data that you expect to be present in the database is missing.
 *
 * This should NOT be used during a public load method to signal to the caller that the data they want is not present. That
 * is a perfect use case for {@link java.util.Optional}. This exception is for more complex use cases such as updating a record
 * that you assume to be present.
 *
 * @author fridge
 */
public class MissingDataException extends DataAccessLayerException {
    public MissingDataException(String message) {
        super(message);
    }
    public MissingDataException(String message, Throwable cause) {
        super(message, cause);
    }
    public MissingDataException(Throwable cause) {
        super(cause);
    }
}
