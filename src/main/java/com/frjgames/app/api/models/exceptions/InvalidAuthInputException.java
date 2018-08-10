package com.frjgames.app.api.models.exceptions;

/**
 * Exception thrown when authenticating the user failed due to invalid params from the caller.
 *
 * @author fridge
 */
public class InvalidAuthInputException extends ClientException {
    public InvalidAuthInputException(String message, Throwable cause) {
        super(message, cause);
    }
    public InvalidAuthInputException(String message) {
        super(message);
    }
    public InvalidAuthInputException(Throwable cause) {
        super(cause);
    }
}
