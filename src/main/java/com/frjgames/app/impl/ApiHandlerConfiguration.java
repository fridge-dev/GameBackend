package com.frjgames.app.impl;

import com.frjgames.app.api.ApiHandler;
import com.frjgames.app.api.AuthenticateUserHandler;
import com.frjgames.app.api.CreateUserHandler;
import com.frjgames.app.password.PasswordModule;
import com.frjgames.app.sessions.SessionModule;
import com.frjgames.app.utils.UniqueIdUtils;
import com.frjgames.dal.interfaces.DataAccessorProvider;
import lombok.Synchronized;

/**
 * Configuration class for instantiating {@link ApiHandler} implementations.
 *
 * @author fridge
 */
public class ApiHandlerConfiguration {

    /**
     * Dependencies
     */
    private final DataAccessorProvider dataAccessorProvider;
    private final UniqueIdUtils uniqueIdUtils = UniqueIdUtils.getInstance();
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
    public ApiHandlerConfiguration(final DataAccessorProvider dataAccessorProvider) {
        this.dataAccessorProvider = dataAccessorProvider;
        this.sessionModule = new SessionModule(dataAccessorProvider.userSessionAccessor());
    }

    @Synchronized
    public CreateUserHandler getCreateUserHandler() {
        if (createUserHandlerSingleton == null) {
            createUserHandlerSingleton = new CreateUserHandlerImpl(
                    uniqueIdUtils,
                    dataAccessorProvider.userAccessor(),
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
                    dataAccessorProvider.userAccessor(),
                    sessionModule.sessionManager(),
                    passwordModule.passwordHasher()
            );
        }

        return authenticateUserHandlerSingleton;
    }

}
