package com.frjgames.app.api.models.exceptions;

/**
 * Exception thrown when authenticating the user failed due to invalid params from the caller.
 *
 * This exception is different from {@link InvalidInputException} because we need to inform user that they've provided
 * valid input (e.g. non-empty strings) but their input has failed validation.
 *
 * @author fridge
 */
public class IncorrectAuthException extends FrjAppException {
    public IncorrectAuthException(String message, Throwable cause) {
        super(message, cause);
    }
    public IncorrectAuthException(String message) {
        super(message);
    }
    public IncorrectAuthException(Throwable cause) {
        super(cause);
    }
}
