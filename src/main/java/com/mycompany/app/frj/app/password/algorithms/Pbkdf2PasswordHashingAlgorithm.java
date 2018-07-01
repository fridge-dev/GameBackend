package com.mycompany.app.frj.app.password.algorithms;

import com.mycompany.app.frj.app.password.models.AlgorithmType;
import com.mycompany.app.frj.app.password.models.CannotPerformHashException;
import com.mycompany.app.frj.app.password.models.PasswordHashParams;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * PBKDF2 (Password-Based Key Derivation Function) using SHA-1 as the hashing function.
 *
 * https://en.wikipedia.org/wiki/PBKDF2
 *
 * Inspired from https://github.com/defuse/password-hashing/blob/master/PasswordStorage.java
 *
 * @author alecva
 */
public class Pbkdf2PasswordHashingAlgorithm implements PasswordHashingAlgorithm {

    private static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";

    private static final int BITS_PER_BYTE = 8;

    /**
     * These will be used for generating new password hashes. They should be safe to change without
     * causing issues for existing users.
     */
    private static final int SALT_BYTE_SIZE = 32;
    private static final int PBKDF_ITERATIONS = 1000;
    private static final int HASH_BYTE_SIZE = 32;

    /**
     * @inheritDoc
     */
    @Override
    public byte[] hash(final String password, final PasswordHashParams params) throws CannotPerformHashException {
        PBEKeySpec keySpec = new PBEKeySpec(
                password.toCharArray(),
                params.getSalt(),
                params.getIterations(),
                params.getHashLength() * BITS_PER_BYTE
        );

        SecretKeyFactory secretKeyFactory;
        try {
            secretKeyFactory = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new CannotPerformHashException("Hashing algorithm not supported.", e);
        }

        SecretKey secretKey;
        try {
            secretKey = secretKeyFactory.generateSecret(keySpec);
        } catch (InvalidKeySpecException e) {
            throw new CannotPerformHashException("Hashing key spec is invalid.", e);
        }

        return secretKey.getEncoded();
    }

    /**
     * @inheritDoc
     */
    @Override
    public PasswordHashParams newHashParams() {
        byte[] salt = new byte[SALT_BYTE_SIZE];
        new SecureRandom().nextBytes(salt);

        return PasswordHashParams.builder()
                .algorithm(AlgorithmType.PBKDF2SHA1)
                .iterations(PBKDF_ITERATIONS)
                .salt(salt)
                .hashLength(HASH_BYTE_SIZE)
                .build();
    }
}
