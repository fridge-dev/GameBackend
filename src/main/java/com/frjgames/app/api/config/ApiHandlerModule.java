package com.frjgames.app.api.config;

import com.frjgames.app.api.models.interfaces.ApiHandler;
import com.frjgames.app.api.models.interfaces.AuthenticateUserSessionHandler;
import com.frjgames.app.api.models.interfaces.CreateGameHandler;
import com.frjgames.app.api.models.interfaces.CreateUserHandler;
import com.frjgames.app.api.models.interfaces.LoginUserHandler;
import com.frjgames.app.api.models.interfaces.MatchGameHandler;
import com.frjgames.app.api.models.interfaces.SaveEverlastHighScore;
import com.frjgames.dal.config.DataAccessLayerModule;

/**
 * Module for getting {@link ApiHandler} classes.
 *
 * @author fridge
 */
public interface ApiHandlerModule {

    CreateUserHandler getCreateUserHandler();

    LoginUserHandler getLoginUserHandler();

    CreateGameHandler getCreateGameHandler();

    MatchGameHandler getMatchGameHandler();

    SaveEverlastHighScore getSaveEverlastHighScore();

    AuthenticateUserSessionHandler getAuthenticateUserSessionHandler();

    /**
     * Factory as part of the interface. Since there's only one choice. :0
     */
    static ApiHandlerModule instantiateModule(final DataAccessLayerModule dataAccessLayerModule) {
        return new ApiHandlerModuleImpl(dataAccessLayerModule);
    }
}
