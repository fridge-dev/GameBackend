package com.frjgames.app.api.handlers.integ;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.frjgames.app.api.handlers.CreateUserHandlerImpl;
import com.frjgames.app.api.handlers.integ.testutils.ApiHandleIntegTestBase;
import com.frjgames.app.api.models.exceptions.DuplicateUsernameException;
import com.frjgames.app.api.models.inputs.CreateUserInput;
import com.frjgames.app.api.models.interfaces.CreateUserHandler;
import com.frjgames.app.api.models.outputs.CreateUserOutput;
import com.frjgames.app.internal.sessions.models.SessionData;
import com.frjgames.dal.ddb.items.UserDdbItem;
import com.frjgames.dal.ddb.items.UserSessionDdbItem;
import com.frjgames.testutils.TestUtilExceptionValidator;
import org.junit.Before;
import org.junit.Test;

/**
 * Functional integ test for {@link CreateUserHandlerImpl} class.
 *
 * @author fridge
 */
public class CreateUserHandlerImplIntegTest extends ApiHandleIntegTestBase {

    private CreateUserHandler createUserHandler;

    public CreateUserHandlerImplIntegTest() {
        super(UserDdbItem.class, UserSessionDdbItem.class);
    }

    @Before
    public void setup() {
        createUserHandler = getApiHandlerModule().getCreateUserHandler();
    }

    @Test
    public void createUser() throws Exception {
        CreateUserInput input = CreateUserInput.builder()
                .username("frj")
                .password("asdf1234")
                .build();

        CreateUserOutput output = createUserHandler.handle(input);

        SessionData sessionToken = output.getSessionToken().orElseThrow(() -> new AssertionError("Optional should be present."));
        assertEquals(input.getUsername(), sessionToken.getUserId());
        assertTrue(sessionToken.getExpirationTimestampMs() > System.currentTimeMillis());
        assertFalse(sessionToken.getPublicSessionToken().isEmpty());
    }

    @Test
    public void createUser_DuplicateUser() throws Exception {
        CreateUserInput input = CreateUserInput.builder()
                .username("frj")
                .password("asdf1234")
                .build();

        createUserHandler.handle(input);

        TestUtilExceptionValidator.assertThrows(DuplicateUsernameException.class, () -> createUserHandler.handle(input));
    }
}