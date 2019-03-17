package com.frjgames.app.api.models.exceptions;

/**
 * An exception for when an invalid update was attempted on a game.
 *
 * @author fridge
 */
public class IllegalGameUpdateException extends FrjAppException {
    public IllegalGameUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalGameUpdateException(String message) {
        super(message);
    }

    public IllegalGameUpdateException(Throwable cause) {
        super(cause);
    }
}
