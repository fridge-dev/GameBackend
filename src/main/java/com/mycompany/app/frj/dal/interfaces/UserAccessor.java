package com.mycompany.app.frj.dal.interfaces;

import com.mycompany.app.frj.dal.models.User;
import com.mycompany.app.frj.dal.models.keys.UserDataAccessKey;

/**
 * Accessor for User data.
 *
 * @author alecva
 */
public interface UserAccessor extends DataAccessor<UserDataAccessKey, User> {

}
