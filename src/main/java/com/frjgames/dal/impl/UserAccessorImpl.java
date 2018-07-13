package com.frjgames.dal.impl;

import com.frjgames.dal.ddb.items.UserDdbItem;
import com.frjgames.dal.models.keys.UserDataKey;
import com.frjgames.dal.ddb.accessors.UserDdbAccessor;
import com.frjgames.dal.interfaces.UserAccessor;
import com.frjgames.dal.models.User;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

/**
 * Database accessor abstraction for User data.
 *
 * @author fridge
 */
@RequiredArgsConstructor
public class UserAccessorImpl implements UserAccessor {

    private final UserDdbAccessor userDdbAccessor;

    /**
     * Create new user.
     */
    @Override
    public void create(final User user) {
        UserDdbItem item = domainTypeToItem(user);

        // Does not handle conditional writes
        userDdbAccessor.createUser(item);
    }

    /**
     * Load user by user name.
     */
    public Optional<User> load(final UserDataKey key) {
        return userDdbAccessor.loadUserByUsername(key.getUsername())
                .map(this::itemToDomainType);
    }

    private UserDdbItem domainTypeToItem(final User user) {
        UserDdbItem item = new UserDdbItem();

        item.setUserId(user.getUserId());
        item.setUsername(user.getUsername());
        item.setPassword(user.getPassword());

        return item;
    }

    private User itemToDomainType(final UserDdbItem item) {
        return User.builder()
                .username(item.getUsername())
                .userId(item.getUserId())
                .password(item.getPassword())
                .build();
    }
}
