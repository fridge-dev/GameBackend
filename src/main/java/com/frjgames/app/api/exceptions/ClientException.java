package com.frjgames.app.api.exceptions;

/**
 * Exception of the caller's fault. Similar to HTTP 4xx.
 *
 * @author fridge
 */
public class ClientException extends Exception {
    public ClientException(final String message, final Throwable cause) {
        super(message, cause);
    }
    public ClientException(final String message) {
        super(message);
    }
    public ClientException(final Throwable cause) {
        super(cause);
    }
}
