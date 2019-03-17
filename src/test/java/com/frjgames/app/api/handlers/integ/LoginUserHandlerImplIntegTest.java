package com.frjgames.app.api.handlers.integ;

import static org.junit.Assert.assertNotNull;

import com.frjgames.app.api.handlers.LoginUserHandlerImpl;
import com.frjgames.app.api.handlers.integ.testutils.ApiHandleIntegTestBase;
import com.frjgames.app.api.models.exceptions.IncorrectAuthException;
import com.frjgames.app.api.models.inputs.CreateUserInput;
import com.frjgames.app.api.models.inputs.LoginUserInput;
import com.frjgames.app.api.models.interfaces.CreateUserHandler;
import com.frjgames.app.api.models.interfaces.LoginUserHandler;
import com.frjgames.app.api.models.outputs.LoginUserOutput;
import com.frjgames.dal.ddb.items.UserDdbItem;
import com.frjgames.dal.ddb.items.UserSessionDdbItem;
import com.frjgames.testutils.TestUtilExceptionValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Functional integration test for the {@link LoginUserHandlerImpl} class.
 *
 * @author fridge
 */
@RunWith(MockitoJUnitRunner.class)
public class LoginUserHandlerImplIntegTest extends ApiHandleIntegTestBase {

    private static final String USERNAME = "lsiuhgralsguhr";
    private static final String PASSWORD = "9f127hl2hi4f12of4";

    private static final LoginUserInput AUTH_INPUT = LoginUserInput.builder()
            .username(USERNAME)
            .password(PASSWORD)
            .build();

    private static final CreateUserInput CREATE_USER_INPUT = CreateUserInput.builder()
            .username(USERNAME)
            .password(PASSWORD)
            .build();

    private LoginUserHandler loginUserHandler;
    private CreateUserHandler createUserHandler;

    public LoginUserHandlerImplIntegTest() {
        super(UserDdbItem.class, UserSessionDdbItem.class);
    }

    @Before
    public void setup() {
        loginUserHandler = getApiHandlerModule().getLoginUserHandler();
        createUserHandler = getApiHandlerModule().getCreateUserHandler();
    }

    @Test
    public void authenticateUser() throws Exception {
        // Attempt when no user exists
        TestUtilExceptionValidator.assertThrows(IncorrectAuthException.class,
                () -> loginUserHandler.handle(AUTH_INPUT)
        );

        // Create user
        createUserHandler.handle(CREATE_USER_INPUT);

        // Attempt with correct password
        LoginUserOutput output = loginUserHandler.handle(AUTH_INPUT);
        assertNotNull(output.getSessionToken());

        // Attempt with incorrect password
        LoginUserInput inputWithIncorrectPassword = LoginUserInput.builder()
                .username(USERNAME)
                .password("incorrect-password")
                .build();
        TestUtilExceptionValidator.assertThrows(IncorrectAuthException.class,
                () -> loginUserHandler.handle(inputWithIncorrectPassword)
        );
    }
}