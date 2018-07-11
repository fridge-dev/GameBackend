package com.mycompany.app.frj.dal.impl;

import com.mycompany.app.frj.dal.ddb.accessors.UserDdbAccessor;
import com.mycompany.app.frj.dal.ddb.items.UserDdbItem;
import com.mycompany.app.frj.dal.interfaces.UserAccessor;
import com.mycompany.app.frj.dal.models.User;
import com.mycompany.app.frj.dal.models.keys.UserDataKey;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

/**
 * Database accessor abstraction for User data.
 *
 * @author alecva
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
