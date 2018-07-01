package com.mycompany.app.frj.app.password.models;

/**
 * Kind of like {@link IllegalArgumentException} when providing a hash to the
 * {@link com.mycompany.app.frj.app.password.PasswordHasher} for verification.
 *
 * @author alecva
 */
public class InvalidHashException extends Exception {
    public InvalidHashException(final String message, final Throwable cause) {
        super(message, cause);
    }
    public InvalidHashException(final String message) {
        super(message);
    }
    public InvalidHashException(final Throwable cause) {
        super(cause);
    }
}
