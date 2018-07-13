package com.frjgames.app.api.exceptions;

/**
 * Exception when application layer has an internal, unrecoverable failure.
 *
 * @author fridge
 */
public class InternalAppException extends Exception {
    public InternalAppException(final String message, final Throwable cause) {
        super(message, cause);
    }
    public InternalAppException(final String message) {
        super(message);
    }
    public InternalAppException(final Throwable cause) {
        super(cause);
    }
}
