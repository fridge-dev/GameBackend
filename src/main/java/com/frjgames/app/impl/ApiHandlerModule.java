package com.frjgames.app.impl;

import com.frjgames.app.api.ApiHandler;
import com.frjgames.app.api.AuthenticateUserHandler;
import com.frjgames.app.api.CreateUserHandler;
import com.frjgames.app.password.PasswordModule;
import com.frjgames.app.sessions.SessionModule;
import com.frjgames.dal.config.DataAccessLayerModule;
import lombok.Synchronized;

/**
 * Configuration class for instantiating {@link ApiHandler} implementations.
 *
 * @author fridge
 */
public class ApiHandlerModule {

    /**
     * Dependencies
     */
    private final DataAccessLayerModule dataAccessLayerModule;
    private final SessionModule sessionModule;
    private final PasswordModule passwordModule = new PasswordModule();

    /**
     * Internal classes
     */
    private CreateUserHandler createUserHandlerSingleton;
    private AuthenticateUserHandler authenticateUserHandlerSingleton;

    /**
     * Instantiate all application layer modules with the low level modules.
     */
    public ApiHandlerModule(final DataAccessLayerModule dataAccessLayerModule) {
        this.dataAccessLayerModule = dataAccessLayerModule;
        this.sessionModule = new SessionModule(dataAccessLayerModule.userSessionAccessor());
    }

    @Synchronized
    public CreateUserHandler getCreateUserHandler() {
        if (createUserHandlerSingleton == null) {
            createUserHandlerSingleton = new CreateUserHandlerImpl(
                    dataAccessLayerModule.userAccessor(),
                    sessionModule.sessionManager(),
                    passwordModule.passwordHasher()
            );
        }

        return createUserHandlerSingleton;
    }

    @Synchronized
    public AuthenticateUserHandler getAuthenticateUserHandler() {
        if (authenticateUserHandlerSingleton == null) {
            authenticateUserHandlerSingleton = new AuthenticateUserHandlerImpl(
                    dataAccessLayerModule.userAccessor(),
                    sessionModule.sessionManager(),
                    passwordModule.passwordHasher()
            );
        }

        return authenticateUserHandlerSingleton;
    }

}
