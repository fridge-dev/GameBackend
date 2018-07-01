package com.mycompany.app.frj.app.config;

import com.mycompany.app.frj.app.auth.AuthTokenGenerator;
import com.mycompany.app.frj.app.impl.CreateUserHandlerImpl;
import com.mycompany.app.frj.app.interfaces.CreateUserHandler;
import com.mycompany.app.frj.app.password.PasswordHasher;
import com.mycompany.app.frj.dal.interfaces.UserAccessor;
import lombok.RequiredArgsConstructor;

/**
 * TODO
 *
 * @author alecva
 */
@RequiredArgsConstructor
public class AppConfig {

    private final UserAccessor userAccessor;

    private final PasswordHasher passwordHasher;

    public CreateUserHandler createUserHandler() {
        AuthTokenGenerator authTokenGenerator = new AuthTokenGenerator();
        // TODO add hasher

        return new CreateUserHandlerImpl(userAccessor, authTokenGenerator, passwordHasher);
    }
}
