package com.frjgames.app.api.models.exceptions;

/**
 * Exception thrown when a match-making game has already been matched.
 *
 * @author alecva
 */
public class GameAlreadyMatchedException extends FrjAppException {
    public GameAlreadyMatchedException(String message, Throwable cause) {
        super(message, cause);
    }

    public GameAlreadyMatchedException(String message) {
        super(message);
    }

    public GameAlreadyMatchedException(Throwable cause) {
        super(cause);
    }
}
