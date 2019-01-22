package com.frjgames.dal.models.interfaces;

import com.frjgames.dal.models.data.PersistedUserSession;
import com.frjgames.dal.models.keys.UserSessionDataKey;

/**
 * Data Accessor for User Session data.
 *
 * @author fridge
 */
public interface UserSessionAccessor extends DataAccessor<UserSessionDataKey, PersistedUserSession> {
    // See parent class
}
