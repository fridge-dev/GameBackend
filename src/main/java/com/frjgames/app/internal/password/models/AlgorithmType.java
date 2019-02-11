package com.frjgames.app.internal.password.models;

/**
 * All supported password-hashing algorithm types.
 *
 * @author fridge
 */
public enum AlgorithmType {

    /**
     * PBKDF (Password-Based Key Derivation Function) using SHA-1 as hashing function.
     *
     * https://en.wikipedia.org/wiki/PBKDF2
     */
    PBKDF2SHA1
}
