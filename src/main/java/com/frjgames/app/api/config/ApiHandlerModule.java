package com.frjgames.app.api.config;

import com.frjgames.app.api.handlers.CreateUserHandlerImpl;
import com.frjgames.app.api.handlers.LoginUserHandlerImpl;
import com.frjgames.app.api.models.interfaces.ApiHandler;
import com.frjgames.app.api.models.interfaces.LoginUserHandler;
import com.frjgames.app.api.models.interfaces.CreateUserHandler;
import com.frjgames.app.internal.password.PasswordModule;
import com.frjgames.app.internal.sessions.SessionModule;
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
    private LoginUserHandler loginUserHandlerSingleton;

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
    public LoginUserHandler getAuthenticateUserHandler() {
        if (loginUserHandlerSingleton == null) {
            loginUserHandlerSingleton = new LoginUserHandlerImpl(
                    dataAccessLayerModule.userAccessor(),
                    sessionModule.sessionManager(),
                    passwordModule.passwordHasher()
            );
        }

        return loginUserHandlerSingleton;
    }

}
