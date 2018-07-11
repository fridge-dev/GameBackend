package com.mycompany.app.frj.dal.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.mycompany.app.frj.dal.config.TestDataAccessConfig;
import com.mycompany.app.frj.dal.ddb.items.UserDdbItem;
import com.mycompany.app.frj.dal.interfaces.UserAccessor;
import com.mycompany.app.frj.dal.models.User;
import com.mycompany.app.frj.dal.models.keys.UserDataKey;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;

/**
 * Functional integration test of the {@link UserAccessorImpl} class.
 *
 * @author alecva
 */
public class UserAccessorImplIntegTest {

    private static final String USER_ID = "souhgaougaasgunjs9hf184";
    private static final String USERNAME = "fridge";
    private static final String PASSWORD = "H^g97R%vk,";

    private UserAccessor userAccessor;

    @Before
    public void setup() {
        TestDataAccessConfig config = new TestDataAccessConfig();
        config.createTable(UserDdbItem.class);

        userAccessor = config.userAccessor();
    }

    @Test
    public void createUser_RetrieveUser() throws Exception {
        User userToWrite = User.builder()
                .userId(USER_ID)
                .username(USERNAME)
                .password(PASSWORD)
                .build();
        UserDataKey userKey = UserDataKey.builder()
                .username(userToWrite.getUsername())
                .build();

        userAccessor.create(userToWrite);

        Optional<User> userOptional = userAccessor.load(userKey);

        assertTrue(userOptional.isPresent());
        User userReadBack = userOptional.get();

        assertEquals(userToWrite, userReadBack);
    }
}