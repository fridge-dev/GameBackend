package com.mycompany.app.frj.app.password;

import com.mycompany.app.frj.app.password.algorithms.PasswordHashingAlgorithm;
import com.mycompany.app.frj.app.password.algorithms.Pbkdf2PasswordHashingAlgorithm;
import com.mycompany.app.frj.app.password.utils.HashParamsEncoder;
import lombok.Synchronized;

/**
 * Configuration for instantiating Password classes.
 *
 * @author alecva
 */
public class PasswordModule {

    private PasswordHasher passwordHasher;

    @Synchronized
    public PasswordHasher passwordHasher() {
        if (passwordHasher == null) {
            passwordHasher = new PasswordHasher(passwordHashingAlgorithm(), new HashParamsEncoder());
        }

        return passwordHasher;
    }

    private PasswordHashingAlgorithm passwordHashingAlgorithm() {
        // Only impl for now is PBKDF
        return new Pbkdf2PasswordHashingAlgorithm();
    }

}
