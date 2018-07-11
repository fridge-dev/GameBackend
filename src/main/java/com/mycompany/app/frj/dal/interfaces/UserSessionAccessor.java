package com.mycompany.app.frj.dal.interfaces;

import com.mycompany.app.frj.dal.models.PersistedUserSession;
import com.mycompany.app.frj.dal.models.keys.UserSessionDataKey;

/**
 * Data Accessor for User Session data.
 *
 * @author alecva
 */
public interface UserSessionAccessor extends DataAccessor<UserSessionDataKey, PersistedUserSession> {
    // See parent class
}
