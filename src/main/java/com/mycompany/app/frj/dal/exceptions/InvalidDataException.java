package com.mycompany.app.frj.dao.exceptions;

/**
 * Exception thrown when data in the DB is in some invalid state.
 *
 * @author alecva
 */
public class InvalidDataException extends RuntimeException {

    public InvalidDataException(final String message) {
        super(message);
    }

}
