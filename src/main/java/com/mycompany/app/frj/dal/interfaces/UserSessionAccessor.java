package com.mycompany.app.frj.dal.interfaces;

import com.mycompany.app.frj.dal.models.UserSession;
import com.mycompany.app.frj.dal.models.keys.UserSessionDataAccessKey;

/**
 * Data Accessor for User Session data.
 *
 * @author alecva
 */
public interface UserSessionAccessor extends DataAccessor<UserSessionDataAccessKey, UserSession> {

}
