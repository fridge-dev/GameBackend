package com.mycompany.app.frj.dal.impl;

import com.mycompany.app.frj.dal.ddb.accessors.UserDdbAccessor;
import com.mycompany.app.frj.dal.ddb.items.UserDdbItem;
import com.mycompany.app.frj.dal.interfaces.UserAccessor;
import com.mycompany.app.frj.dal.models.User;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

/**
 * TODO
 *
 * @author alecva
 */
@RequiredArgsConstructor
public class UserAccessorImpl implements UserAccessor {

    private final UserDdbAccessor userDdbAccessor;

    /**
     * TODO
     */
    public void createNewUser(final User user) {
        UserDdbItem item = domainTypeToItem(user);

        userDdbAccessor.createUser(item);
    }

    /**
     * TODO
     */
    public Optional<User> getUserByUsername(final String username) {
        return userDdbAccessor.loadUserByUsername(username)
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
