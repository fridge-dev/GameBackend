package com.frjgames.dal.models.exceptions;

/**
 * Exception thrown when there is an issue serializing data in the DAL.
 *
 * @author fridge
 */
public class DataSerializationException extends DataAccessLayerException {
    public DataSerializationException(String message) {
        super(message);
    }
    public DataSerializationException(String message, Throwable cause) {
        super(message, cause);
    }
    public DataSerializationException(Throwable cause) {
        super(cause);
    }
}
