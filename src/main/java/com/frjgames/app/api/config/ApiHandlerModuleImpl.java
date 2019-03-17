package com.frjgames.app.api.config;

import com.frjgames.app.api.handlers.CreateUserHandlerImpl;
import com.frjgames.app.api.handlers.GameHandlerImpl;
import com.frjgames.app.api.handlers.LoginUserHandlerImpl;
import com.frjgames.app.api.models.interfaces.ApiHandler;
import com.frjgames.app.api.models.interfaces.AuthenticateUserSessionHandler;
import com.frjgames.app.api.models.interfaces.CreateGameHandler;
import com.frjgames.app.api.models.interfaces.CreateUserHandler;
import com.frjgames.app.api.models.interfaces.LoginUserHandler;
import com.frjgames.app.api.models.interfaces.MatchGameHandler;
import com.frjgames.app.api.models.interfaces.SaveEverlastHighScore;
import com.frjgames.app.internal.password.PasswordModule;
import com.frjgames.app.internal.sessions.SessionModule;
import com.frjgames.dal.config.DataAccessLayerModule;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * Configuration class for instantiating {@link ApiHandler} implementations.
 *
 * @author fridge
 */
/* NOT PUBLIC */ class ApiHandlerModuleImpl implements ApiHandlerModule {

    /**
     * Dependency modules.
     */
    private final DataAccessLayerModule dataAccessLayerModule;
    private final SessionModule sessionModule;
    private final PasswordModule passwordModule;

    /**
     * {@link ApiHandler} implementations.
     */
    @Getter(lazy = true)
    private final CreateUserHandler createUserHandler = instantiateCreateUserHandler();

    @Getter(lazy = true)
    private final LoginUserHandler loginUserHandler = instantiateLoginUserHandler();

    @Getter(lazy = true)
    private final CreateGameHandler createGameHandler = getGameHandlerImpl();

    @Getter(lazy = true)
    private final MatchGameHandler matchGameHandler = getGameHandlerImpl();

    @Getter(lazy = true, value = AccessLevel.PRIVATE)
    private final GameHandlerImpl gameHandlerImpl = instantiateGameHandlerImpl();

    @Getter(lazy = true)
    private final SaveEverlastHighScore saveEverlastHighScore = unsupported();

    @Getter(lazy = true)
    private final AuthenticateUserSessionHandler authenticateUserSessionHandler = unsupported();

    /**
     * Instantiate all application layer modules with the low level modules.
     *
     * Service layer should only specify the database module.
     */
    /* NOT PUBLIC */ ApiHandlerModuleImpl(final DataAccessLayerModule dataAccessLayerModule) {
        this.dataAccessLayerModule = dataAccessLayerModule;
        this.sessionModule = new SessionModule(dataAccessLayerModule.userSessionAccessor());
        this.passwordModule = new PasswordModule();
    }

    private CreateUserHandler instantiateCreateUserHandler() {
        return new CreateUserHandlerImpl(
                dataAccessLayerModule.userAccessor(),
                sessionModule.sessionManager(),
                passwordModule.passwordHasher()
        );
    }

    private LoginUserHandler instantiateLoginUserHandler() {
        return new LoginUserHandlerImpl(
                dataAccessLayerModule.userAccessor(),
                sessionModule.sessionManager(),
                passwordModule.passwordHasher()
        );
    }

    private GameHandlerImpl instantiateGameHandlerImpl() {
        return new GameHandlerImpl(dataAccessLayerModule.matchMadeGameAccessor());
    }

    private static <T> T unsupported() {
        throw new UnsupportedOperationException("ApiHandler is unsupported.");
    }
}
