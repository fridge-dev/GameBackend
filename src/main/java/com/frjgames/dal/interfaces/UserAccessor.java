package com.frjgames.dal.interfaces;

import com.frjgames.dal.models.keys.UserDataKey;
import com.frjgames.dal.models.User;

/**
 * Accessor for User data.
 *
 * @author fridge
 */
public interface UserAccessor extends DataAccessor<UserDataKey, User> {
    // See parent class
}
