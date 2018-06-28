package com.mycompany.app.frj.dal.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.mycompany.app.frj.dal.config.DataAccessConfig;
import com.mycompany.app.frj.dal.interfaces.UserAccessor;
import com.mycompany.app.frj.dal.models.User;
import java.util.Optional;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * TODO
 *
 * @author alecva
 */
@Ignore
public class UserAccessorImplIntegTest {

    private static final String USERNAME = "fridge";
    private static final String PASSWORD = "H^g97R%vk,";

    private UserAccessor userAccessor;

    @Before
    public void setup() {
        DataAccessConfig config = new DataAccessConfig(RandomStringUtils.random(30));
        userAccessor = config.userAccessor();
    }

    @Test
    public void createUser_RetrieveUser() throws Exception {
        User userToWrite = User.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .build();

        userAccessor.createNewUser(userToWrite);

        Optional<User> userOptional = userAccessor.getUserByUsername(userToWrite.getUsername());

        assertTrue(userOptional.isPresent());
        User userReadBack = userOptional.get();

        assertEquals(userToWrite.getUsername(), userReadBack.getUsername());
        assertNotEquals(userToWrite.getPassword(), userReadBack.getPassword());
        assertNotNull(userReadBack.getUserId());
    }

}