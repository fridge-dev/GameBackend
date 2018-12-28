package com.frjgames.dal.interfaces;

/**
 * Config interface for Data Access Layer (DAL).
 *
 * @author fridge
 */
public interface DataAccessorProvider {

    UserAccessor userAccessor();

    UserSessionAccessor userSessionAccessor();

    EverlastHighScoreAccessor everlastHighScoreAccessor();
}
