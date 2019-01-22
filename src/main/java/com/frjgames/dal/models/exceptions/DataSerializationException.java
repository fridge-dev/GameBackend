package com.frjgames.dal.models.exceptions;

/**
 * TODO
 *
 * @author TODO
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
