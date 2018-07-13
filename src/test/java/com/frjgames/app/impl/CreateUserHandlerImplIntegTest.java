package com.frjgames.app.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.frjgames.app.api.CreateUserHandler;
import com.frjgames.app.api.models.CreateUserInput;
import com.frjgames.app.api.models.CreateUserOutput;
import com.frjgames.app.sessions.models.SessionData;
import com.frjgames.dal.config.TestDataAccessConfig;
import com.frjgames.dal.ddb.items.UserDdbItem;
import com.frjgames.dal.ddb.items.UserSessionDdbItem;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Functional integ test for {@link CreateUserHandlerImpl} class.
 *
 * @author alecva
 */
public class CreateUserHandlerImplIntegTest {

    private CreateUserHandler createUserHandler;

    @Before
    public void setup() {
        TestDataAccessConfig dataAccessConfig = new TestDataAccessConfig(UserDdbItem.class, UserSessionDdbItem.class);
        ApiHandlerConfiguration apiHandlerConfiguration = new ApiHandlerConfiguration(dataAccessConfig);

        createUserHandler = apiHandlerConfiguration.getCreateUserHandler();
    }

    @Test
    public void createUser() throws Exception {
        CreateUserInput input = CreateUserInput.builder()
                .username("frj")
                .password("asdf1234")
                .build();

        CreateUserOutput output = createUserHandler.handleCreateUser(input);

        SessionData sessionToken = output.getSessionToken();
        assertEquals(output.getUserId(), sessionToken.getUserId());
        assertTrue(sessionToken.getExpirationTimestampMs() > System.currentTimeMillis());
    }

    @Test
    @Ignore("TODO This is the next challenge")
    public void createUser_DuplicateUser() throws Exception {
        CreateUserInput input = CreateUserInput.builder()
                .username("frj")
                .password("asdf1234")
                .build();

        createUserHandler.handleCreateUser(input);

        // This should throw appropriate exception.
        createUserHandler.handleCreateUser(input);
    }
}