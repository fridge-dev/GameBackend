package com.frjgames.app.internal.password.models;

import com.frjgames.app.internal.password.PasswordHasher;

/**
 * Exception thrown when {@link PasswordHasher} fails to perform a hash.
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
