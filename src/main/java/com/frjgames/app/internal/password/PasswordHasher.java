package com.frjgames.app.internal.password;

import com.frjgames.app.internal.password.models.CannotPerformHashException;
import com.frjgames.app.internal.password.models.InvalidHashException;

/**
 * This class is responsible for creating a secure hash of a password that can be stored in a database and used for user authentication.
 * It is not just cryptographically secure hashing, but specializes in hashing of PASSWORDS.
 *
 * @author fridge
 */
public interface PasswordHasher {

    /**
     * Creates a hashed version of the provided password that is ready/safe to store in a database. The returned hash is already securely
     * encoded with salt and other data needed to verify a login attempt with another password.
     *
     * The returned hash should be used in the method {@link #matches(String, String)} to verify a login.
     */
    String createStorableHash(final String password) throws InvalidHashException, CannotPerformHashException;

    /**
     * Determine if the provided password matches the provided hash. This can be used to verify a user login attempt (password) against
     * the correct password hash stored in a database. It is assumed that the provided "correct" hash is encoded by what was returned
     * from the {@link #createStorableHash(String)} method.
     */
    boolean matches(final String rawPassword, final String correctHash) throws InvalidHashException, CannotPerformHashException;

}
