package com.mycompany.app.frj.dal;

import com.mycompany.app.frj.dal.ddb.accessors.UserDdbAccessor;
import com.mycompany.app.frj.dal.ddb.items.UserDdbItem;
import com.mycompany.app.frj.dal.interfaces.UserAccessor;
import com.mycompany.app.frj.dal.models.User;
import com.mycompany.app.frj.dal.utils.SecureHashUtil;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

/**
 * TODO
 *
 * @author alecva
 */
@RequiredArgsConstructor
public class UserAccessorImpl implements UserAccessor {

    private final UserDdbAccessor userDdbAccessor;

    private final SecureHashUtil hashUtil;

    public void createNewUser(final User user) {
        UserDdbItem item = new UserDdbItem();

        String userId = newUserId();

        item.setUserId(userId);
        item.setUsername(user.getUsername());
        item.setPassword(hashPassword(user.getPassword(), userId));

        userDdbAccessor.createUser(item);
    }

    private String newUserId() {
        return UUID.randomUUID().toString();
    }

    private String hashPassword(final String password, final String userId) {
        return hashUtil.hash(password, userId);
    }

    /**
     * TODO
     */
    public Optional<User> getUserByUsername(final String username) {
        return userDdbAccessor.loadUserByUsername(username)
                .map(this::itemToDomainType);
    }

    private User itemToDomainType(final UserDdbItem item) {
        return User.builder()
                .username(item.getUsername())
                .userId(item.getUserId())
                .password(item.getPassword())
                .build();
    }
}
