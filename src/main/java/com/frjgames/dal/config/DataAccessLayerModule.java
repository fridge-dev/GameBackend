package com.frjgames.dal.config;

import com.frjgames.dal.models.interfaces.EverlastHighScoreAccessor;
import com.frjgames.dal.models.interfaces.GameAccessor;
import com.frjgames.dal.models.interfaces.MatchMadeGameAccessor;
import com.frjgames.dal.models.interfaces.UserAccessor;
import com.frjgames.dal.models.interfaces.UserSessionAccessor;

/**
 * Module interface for Data Access Layer (DAL).
 *
 * The DAL is the layer of abstraction for the application database.
 *
 * @author fridge
 */
public interface DataAccessLayerModule {

    UserAccessor userAccessor();

    UserSessionAccessor userSessionAccessor();

    EverlastHighScoreAccessor everlastHighScoreAccessor();

    MatchMadeGameAccessor matchMadeGameAccessor();

    GameAccessor gameAccessor();
}
