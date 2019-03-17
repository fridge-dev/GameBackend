package com.frjgames.app.api.models.exceptions;

/**
 * Exception thrown when caller has provided invalid inputs to the top level API.
 *
 * @author fridge
 */
public class InvalidInputException extends FrjAppException {
    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }
    public InvalidInputException(String message) {
        super(message);
    }
    public InvalidInputException(Throwable cause) {
        super(cause);
    }
}
