package com.mycompany.app.frj.dal.interfaces;

import com.mycompany.app.frj.dal.models.User;
import java.util.Optional;

/**
 * TODO
 *
 * @author alecva
 */
public interface UserAccessor {

    /**
     * TODO
     */
    void createNewUser(final User user);

    /**
     * TODO
     */
    Optional<User> getUserByUsername(final String username);

}
