package com.mycompany.app.frj.app.impl;

import com.mycompany.app.frj.app.api.CreateUserHandler;
import com.mycompany.app.frj.app.password.PasswordModule;
import com.mycompany.app.frj.app.sessions.SessionModule;
import com.mycompany.app.frj.app.utils.UniqueIdUtils;
import com.mycompany.app.frj.dal.interfaces.DataAccessorProvider;
import lombok.Synchronized;

/**
 * Configuration class for instantiating {@link com.mycompany.app.frj.app.api.ApiHandler} implementations.
 *
 * @author alecva
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

}
