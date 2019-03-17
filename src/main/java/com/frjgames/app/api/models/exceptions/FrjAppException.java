package com.frjgames.app.api.models.exceptions;

/**
 * Base exception class that all {@link com.frjgames.app.api.models.interfaces.ApiHandler} can throw.
 *
 * @author fridge
 */
public abstract class FrjAppException extends Exception {
    public FrjAppException(final String message, final Throwable cause) {
        super(message, cause);
    }
    public FrjAppException(final String message) {
        super(message);
    }
    public FrjAppException(final Throwable cause) {
        super(cause);
    }
}
