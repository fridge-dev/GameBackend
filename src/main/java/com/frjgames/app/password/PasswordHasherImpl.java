package com.frjgames.app.password;

import com.frjgames.app.password.models.PasswordHashParams;
import com.frjgames.app.password.algorithms.PasswordHashingAlgorithm;
import com.frjgames.app.password.models.CannotPerformHashException;
import com.frjgames.app.password.models.InvalidHashException;
import com.frjgames.app.password.utils.HashParamsEncoder;
import lombok.RequiredArgsConstructor;

/**
 * This class is responsible for creating a secure hash of a password that can be stored in a database and used for user authentication.
 * It is not just cryptographically secure hashing, but specializes in hashing of PASSWORDS.
 *
 * This is the top-level entry point of the password package.
 *
 * Research:
 * - https://crackstation.net/hashing-security.htm
 * - https://stackoverflow.com/questions/2860943/how-can-i-hash-a-password-in-java
 * - https://github.com/defuse/password-hashing
 * - https://github.com/jedisct1/libsodium
 *
 * @author fridge
 */
@RequiredArgsConstructor
public class PasswordHasherImpl implements PasswordHasher {

    private final PasswordHashingAlgorithm algorithm;

    private final HashParamsEncoder hashEncoder;

    /**
     * Creates a hashed version of the provided password that is ready/safe to store in a database. The returned hash is already securely
     * encoded with salt and other data needed to verify a login attempt with another password.
     *
     * The returned hash should be used in the method {@link #matches(String, String)} to verify a login.
     */
    public String createStorableHash(final String password) throws InvalidHashException, CannotPerformHashException {
        PasswordHashParams paramsWithHash = createHash(password);

        return hashEncoder.encodeHash(paramsWithHash);
    }

    private PasswordHashParams createHash(final String password) throws CannotPerformHashException {
        PasswordHashParams params = algorithm.newHashParams();

        byte[] hash = algorithm.hash(password, params);

        return params.toBuilder()
                .hash(hash)
                .build();
    }

    /**
     * Determine if the provided password matches the provided hash. This can be used to verify a user login attempt (password) against
     * the correct password hash stored in a database. It is assumed that the provided "correct" hash is encoded by what was returned
     * from the {@link #createStorableHash(String)} method.
     */
    public boolean matches(final String rawPassword, final String correctHash) throws InvalidHashException, CannotPerformHashException {
        PasswordHashParams params = hashEncoder.decodeHash(correctHash);

        byte[] actualHash = algorithm.hash(rawPassword, params);

        return slowEquals(params.getHash(), actualHash);
    }

    /**
     * This intentionally avoids short-circuit equals, such that the time to execute this method
     * should be the same, regardless of when the first non-equal byte is found.
     */
    private boolean slowEquals(byte[] a, byte[] b) {
        // XOR = 0 if bytes are the same
        int xor = a.length ^ b.length;
        for (int i = 0; i < a.length && i < b.length; i++) {
            xor |= a[i] ^ b[i];
        }
        return xor == 0;
    }

}
