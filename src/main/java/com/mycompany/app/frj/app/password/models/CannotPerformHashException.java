package com.mycompany.app.frj.app.password.models;

/**
 * Exception thrown when {@link com.mycompany.app.frj.app.password.PasswordHasher} fails to perform a hash.
 *
 * @author alecva
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
