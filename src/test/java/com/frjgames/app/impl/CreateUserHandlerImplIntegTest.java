package com.frjgames.app.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.frjgames.app.api.CreateUserHandler;
import com.frjgames.app.api.models.inputs.CreateUserInput;
import com.frjgames.app.api.models.outputs.CreateUserOutput;
import com.frjgames.app.sessions.models.SessionData;
import com.frjgames.dal.ddb.items.UserDdbItem;
import com.frjgames.dal.ddb.items.UserSessionDdbItem;
import com.frjgames.dal.ddb.testutils.TestUtilDynamoDbLocalTestBase;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Functional integ test for {@link CreateUserHandlerImpl} class.
 *
 * @author fridge
 */
public class CreateUserHandlerImplIntegTest extends TestUtilDynamoDbLocalTestBase {

    private CreateUserHandler createUserHandler;

    public CreateUserHandlerImplIntegTest() {
        super(UserDdbItem.class, UserSessionDdbItem.class);
    }

    @Before
    public void setup() {
        ApiHandlerModule apiHandlerModule = new ApiHandlerModule(getModule());

        createUserHandler = apiHandlerModule.getCreateUserHandler();
    }

    @Test
    public void createUser() throws Exception {
        CreateUserInput input = CreateUserInput.builder()
                .username("frj")
                .password("asdf1234")
                .build();

        CreateUserOutput output = createUserHandler.handle(input);

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

        createUserHandler.handle(input);

        // This should throw appropriate exception.
        createUserHandler.handle(input);
    }
}