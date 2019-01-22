package com.frjgames.app.password.models;

import com.frjgames.app.password.PasswordHasherImpl;

/**
 * Exception thrown when {@link PasswordHasherImpl} fails to perform a hash.
 *
 * @author fridge
 */
public class CannotPerformHashException extends Exception {
    public CannotPerformHashException(final String message, final Throwable cause) {
        super(message, cause);
    }
    public CannotPerformHashException(final String message) {
        super(message);
    }
    public CannotPerformHashException(final Throwable cause) {
        super(cause);
    }
}
