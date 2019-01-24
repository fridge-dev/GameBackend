package com.frjgames.app.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.frjgames.app.api.models.exceptions.DuplicateUsernameException;
import com.frjgames.app.api.models.exceptions.InternalAppException;
import com.frjgames.app.api.models.inputs.CreateUserInput;
import com.frjgames.app.api.models.outputs.CreateUserOutput;
import com.frjgames.app.password.PasswordHasherImpl;
import com.frjgames.app.password.models.CannotPerformHashException;
import com.frjgames.app.password.models.InvalidHashException;
import com.frjgames.app.sessions.SessionManager;
import com.frjgames.app.sessions.models.CreateSessionInput;
import com.frjgames.app.sessions.models.SessionData;
import com.frjgames.dal.models.data.User;
import com.frjgames.dal.models.exceptions.ConditionalWriteException;
import com.frjgames.dal.models.interfaces.UserAccessor;
import com.frjgames.dal.models.keys.UserDataKey;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Tests the {@link CreateUserHandlerImpl} class.
 *
 * @author fridge
 */
@RunWith(MockitoJUnitRunner.class)
public class CreateUserHandlerImplTest {

    private static final String USERNAME = "fridge_guy_XX";
    private static final String PASSWORD = "pass123";
    private static final String HASHED_PASSWORD = "3zw4ex5rc6tv7yb8un";

    @InjectMocks
    private CreateUserHandlerImpl target;

    @Mock
    private UserAccessor injectedUserAccessor;

    @Mock
    private SessionManager injectedSessionManager;

    @Mock
    private PasswordHasherImpl injectedPasswordHasher;

    @Mock
    private SessionData mockSession;

    private CreateUserInput input;
    private ArgumentCaptor<User> createdUser;
    private ArgumentCaptor<CreateSessionInput> sessionInputCaptor;

    @Before
    public void setup() throws Exception {
        input = CreateUserInput.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .build();

        stubPasswordHasher();
        stubUserAccessorCreate();
        stubUserAccessorLoad();
        stubSessionManagerCreate();
    }

    private void stubUserAccessorCreate() {
        createdUser = ArgumentCaptor.forClass(User.class);
        doNothing().when(injectedUserAccessor).create(createdUser.capture());
    }

    private void stubPasswordHasher() throws Exception {
        when(injectedPasswordHasher.createStorableHash(PASSWORD)).thenReturn(HASHED_PASSWORD);
    }

    private void stubUserAccessorLoad() {
        UserDataKey expectedKey = UserDataKey.builder()
                .username(USERNAME)
                .build();

        User stubbedUser = User.builder()
                .username(USERNAME)
                .build();

        when(injectedUserAccessor.load(expectedKey)).thenReturn(Optional.of(stubbedUser));
    }

    private void stubSessionManagerCreate() {
        sessionInputCaptor = ArgumentCaptor.forClass(CreateSessionInput.class);
        when(injectedSessionManager.createSession(sessionInputCaptor.capture())).thenReturn(mockSession);
    }

    @Test
    public void handleCreateUser() throws Exception {
        // Method under test
        CreateUserOutput output = target.handle(input);

        // Validate
        CreateUserOutput expectedOutput = CreateUserOutput.builder()
                .sessionToken(mockSession)
                .build();
        assertEquals(expectedOutput, output);

        User expectedUser = User.builder()
                .username(USERNAME)
                .password(HASHED_PASSWORD)
                .build();
        assertEquals(expectedUser, createdUser.getValue());

        assertEquals(USERNAME, sessionInputCaptor.getValue().getUserId());
    }

    @Test(expected = InternalAppException.class)
    public void handleCreateUser_InvalidHash() throws Exception {
        when(injectedPasswordHasher.createStorableHash(PASSWORD)).thenThrow(new InvalidHashException("fake"));

        // Method under test
        target.handle(input);
    }

    @Test(expected = InternalAppException.class)
    public void handleCreateUser_HashingFailure() throws Exception {
        when(injectedPasswordHasher.createStorableHash(PASSWORD)).thenThrow(new CannotPerformHashException("fake"));

        // Method under test
        target.handle(input);
    }

    @Test(expected = DuplicateUsernameException.class)
    public void handleCreateUser_UsernameAlreadyExists() throws Exception {
        doThrow(new ConditionalWriteException("fake")).when(injectedUserAccessor).create(any());

        // Method under test
        target.handle(input);
    }

    @Test
    public void handleCreateUser_CreateSessionFailure() throws Exception {
        when(injectedSessionManager.createSession(any())).thenThrow(new RuntimeException("fake"));

        CreateUserOutput output = target.handle(input);

        assertFalse(output.getSessionToken().isPresent());
    }

}