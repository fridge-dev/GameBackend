package com.frjgames.dal.models.interfaces;

import com.frjgames.dal.models.keys.UserDataKey;
import com.frjgames.dal.models.data.User;

/**
 * Accessor for User data.
 *
 * @author fridge
 */
public interface UserAccessor extends DataAccessor<UserDataKey, User> {
    // See parent class
}
