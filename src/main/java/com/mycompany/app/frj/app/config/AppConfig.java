package com.mycompany.app.frj.app.config;

import com.mycompany.app.frj.app.auth.AuthTokenGenerator;
import com.mycompany.app.frj.app.impl.CreateUserHandlerImpl;
import com.mycompany.app.frj.app.interfaces.CreateUserHandler;
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

    public CreateUserHandler createUserHandler() {
        AuthTokenGenerator authTokenGenerator = new AuthTokenGenerator();

        return new CreateUserHandlerImpl(userAccessor, authTokenGenerator);
    }
}
