package com.mycompany.app.frj.app.password.models;

/**
 * All supported password-hashing algorithm types.
 *
 * @author alecva
 */
public enum AlgorithmType {

    /**
     * PBKDF (Password-Based Key Derivation Function) using SHA-1 as hashing function.
     *
     * https://en.wikipedia.org/wiki/PBKDF2
     */
    PBKDF2SHA1
}
