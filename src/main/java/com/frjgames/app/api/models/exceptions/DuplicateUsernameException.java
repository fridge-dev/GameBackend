package com.frjgames.app.api.models.exceptions;

/**
 * Exception thrown when a client attempts to create a user account for username that already exists.
 *
 * @author fridge
 */
public class DuplicateUsernameException extends ClientException {
    public DuplicateUsernameException(String message, Throwable cause) {
        super(message, cause);
    }
    public DuplicateUsernameException(String message) {
        super(message);
    }
    public DuplicateUsernameException(Throwable cause) {
        super(cause);
    }
}
