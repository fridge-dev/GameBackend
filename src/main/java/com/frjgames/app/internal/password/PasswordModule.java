package com.frjgames.app.internal.password;

import com.frjgames.app.internal.password.algorithms.PasswordHashingAlgorithm;
import com.frjgames.app.internal.password.algorithms.Pbkdf2PasswordHashingAlgorithm;
import com.frjgames.app.internal.password.utils.HashParamsEncoder;
import lombok.Synchronized;

/**
 * Configuration for instantiating Password classes.
 *
 * @author fridge
 */
public class PasswordModule {

    private PasswordHasher passwordHasher;

    @Synchronized
    public PasswordHasher passwordHasher() {
        if (passwordHasher == null) {
            passwordHasher = new PasswordHasherImpl(passwordHashingAlgorithm(), new HashParamsEncoder());
        }

        return passwordHasher;
    }

    private PasswordHashingAlgorithm passwordHashingAlgorithm() {
        // Only impl for now is PBKDF
        return new Pbkdf2PasswordHashingAlgorithm();
    }

}
