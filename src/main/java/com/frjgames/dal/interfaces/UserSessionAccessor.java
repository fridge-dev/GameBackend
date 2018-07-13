package com.frjgames.dal.interfaces;

import com.frjgames.dal.models.PersistedUserSession;
import com.frjgames.dal.models.keys.UserSessionDataKey;

/**
 * Data Accessor for User Session data.
 *
 * @author fridge
 */
public interface UserSessionAccessor extends DataAccessor<UserSessionDataKey, PersistedUserSession> {
    // See parent class
}
