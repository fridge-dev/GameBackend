package com.frjgames.app.impl;

import static org.junit.Assert.assertNotNull;

import com.frjgames.app.api.AuthenticateUserHandler;
import com.frjgames.app.api.CreateUserHandler;
import com.frjgames.app.api.models.exceptions.IncorrectAuthException;
import com.frjgames.app.api.models.inputs.AuthenticateUserInput;
import com.frjgames.app.api.models.inputs.CreateUserInput;
import com.frjgames.app.api.models.outputs.AuthenticateUserOutput;
import com.frjgames.dal.ddb.items.UserDdbItem;
import com.frjgames.dal.ddb.items.UserSessionDdbItem;
import com.frjgames.dal.ddb.testutils.TestUtilDynamoDbLocalTestBase;
import com.frjgames.testutils.TestUtilExceptionValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Functional integration test for the {@link AuthenticateUserHandlerImpl} class.
 *
 * @author fridge
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthenticateUserHandlerImplIntegTest extends TestUtilDynamoDbLocalTestBase {

    private static final String USERNAME = "lsiuhgralsguhr";
    private static final String PASSWORD = "9f127hl2hi4f12of4";

    private static final AuthenticateUserInput AUTH_INPUT = AuthenticateUserInput.builder()
            .username(USERNAME)
            .password(PASSWORD)
            .build();

    private static final CreateUserInput CREATE_USER_INPUT = CreateUserInput.builder()
            .username(USERNAME)
            .password(PASSWORD)
            .build();

    private AuthenticateUserHandler authenticateUserHandler;
    private CreateUserHandler createUserHandler;

    public AuthenticateUserHandlerImplIntegTest() {
        super(UserDdbItem.class, UserSessionDdbItem.class);
    }

    @Before
    public void setup() {
        ApiHandlerModule apiHandlerModule = new ApiHandlerModule(getDalModule());

        authenticateUserHandler = apiHandlerModule.getAuthenticateUserHandler();
        createUserHandler = apiHandlerModule.getCreateUserHandler();
    }

    @Test
    public void authenticateUser() throws Exception {
        // Attempt when no user exists
        TestUtilExceptionValidator.validateThrown(IncorrectAuthException.class,
                () -> authenticateUserHandler.handle(AUTH_INPUT)
        );

        // Create user
        createUserHandler.handle(CREATE_USER_INPUT);

        // Attempt with correct password
        AuthenticateUserOutput output = authenticateUserHandler.handle(AUTH_INPUT);
        assertNotNull(output.getSessionToken());

        // Attempt with incorrect password
        AuthenticateUserInput inputWithIncorrectPassword = AuthenticateUserInput.builder()
                .username(USERNAME)
                .password("incorrect-password")
                .build();
        TestUtilExceptionValidator.validateThrown(IncorrectAuthException.class,
                () -> authenticateUserHandler.handle(inputWithIncorrectPassword)
        );
    }
}