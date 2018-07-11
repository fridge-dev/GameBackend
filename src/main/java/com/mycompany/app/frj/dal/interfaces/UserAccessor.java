package com.mycompany.app.frj.dal.interfaces;

import com.mycompany.app.frj.dal.models.User;
import com.mycompany.app.frj.dal.models.keys.UserDataKey;

/**
 * Accessor for User data.
 *
 * @author alecva
 */
public interface UserAccessor extends DataAccessor<UserDataKey, User> {
    // See parent class
}
