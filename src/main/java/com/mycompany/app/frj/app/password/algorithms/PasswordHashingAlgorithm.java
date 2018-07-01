package com.mycompany.app.frj.app.password.algorithms;

import com.mycompany.app.frj.app.password.models.CannotPerformHashException;
import com.mycompany.app.frj.app.password.models.PasswordHashParams;

/**
 * A PasswordHashingAlgorithm is a specific implementation of a cryptographically secure method of
 * generating hashes of user password that are safe to store in persistent storage.
 *
 * @author alecva
 */
public interface PasswordHashingAlgorithm {

    /**
     * Given the password and hashing configuration params, produce a digest using the underlying crypto algorithm.
     */
    byte[] hash(String password, PasswordHashParams params) throws CannotPerformHashException;

    /**
     * Create the params for the underlying crypto algorithm to use when producing a hash.
     */
    PasswordHashParams newHashParams();
}
