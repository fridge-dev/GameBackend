package com.frjgames.app.api.models.exceptions;

/**
 * There was a duplicate f%#*ing game! De-dupe that shit.
 *
 * @author fridge
 */
public class DuplicateGameException extends FrjAppException {
    public DuplicateGameException(String message, Throwable cause) {
        super(message, cause);
    }
    public DuplicateGameException(String message) {
        super(message);
    }
    public DuplicateGameException(Throwable cause) {
        super(cause);
    }
}
