package com.frjgames.app.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.frjgames.app.api.exceptions.InternalAppException;
import com.frjgames.app.api.models.CreateUserInput;
import com.frjgames.app.api.models.CreateUserOutput;
import com.frjgames.app.password.PasswordHasher;
import com.frjgames.app.password.models.InvalidHashException;
import com.frjgames.app.sessions.SessionManager;
import com.frjgames.dal.interfaces.UserAccessor;
import com.frjgames.dal.models.keys.UserDataKey;
import com.frjgames.app.password.models.CannotPerformHashException;
import com.frjgames.app.sessions.models.CreateSessionInput;
import com.frjgames.app.sessions.models.SessionData;
import com.frjgames.app.utils.UniqueIdUtils;
import com.frjgames.dal.models.User;
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
 * @author alecva
 */
@RunWith(MockitoJUnitRunner.class)
public class CreateUserHandlerImplTest {

    private static final String USERNAME = "fridge_guy_XX";
    private static final String PASSWORD = "pass123";
    private static final String HASHED_PASSWORD = "3zw4ex5rc6tv7yb8un";
    private static final String USER_ID = "mklnjoih98y&VUGTF&5efgtxSE%#Q";

    @InjectMocks
    private CreateUserHandlerImpl target;

    @Mock
    private UniqueIdUtils injectedUniqueIdUtils;

    @Mock
    private UserAccessor injectedUserAccessor;

    @Mock
    private SessionManager injectedSessionManager;

    @Mock
    private PasswordHasher injectedPasswordHasher;

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

        stubUniqueIdUtils();
        stubPasswordHasher();
        stubUserAccessorCreate();
        stubUserAccessorLoad();
        stubSessionManagerCreate();
    }

    private void stubUserAccessorCreate() {
        createdUser = ArgumentCaptor.forClass(User.class);
        doNothing().when(injectedUserAccessor).create(createdUser.capture());
    }

    private void stubUniqueIdUtils() {
        when(injectedUniqueIdUtils.newUserId()).thenReturn(USER_ID);
    }

    private void stubPasswordHasher() throws Exception {
        when(injectedPasswordHasher.createStorableHash(PASSWORD)).thenReturn(HASHED_PASSWORD);
    }

    private void stubUserAccessorLoad() {
        UserDataKey expectedKey = UserDataKey.builder()
                .username(USERNAME)
                .build();

        User stubbedUser = User.builder()
                .userId(USER_ID)
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
        CreateUserOutput output = target.handleCreateUser(input);

        // Validate
        CreateUserOutput expectedOutput = CreateUserOutput.builder()
                .userId(USER_ID)
                .sessionToken(mockSession)
                .build();
        assertEquals(expectedOutput, output);

        User expectedUser = User.builder()
                .userId(USER_ID)
                .username(USERNAME)
                .password(HASHED_PASSWORD)
                .build();
        assertEquals(expectedUser, createdUser.getValue());

        assertEquals(USER_ID, sessionInputCaptor.getValue().getUserId());
    }

    @Test(expected = InternalAppException.class)
    public void handleCreateUser_InvalidHash() throws Exception {
        when(injectedPasswordHasher.createStorableHash(PASSWORD)).thenThrow(new InvalidHashException("fake"));

        // Method under test
        target.handleCreateUser(input);
    }

    @Test(expected = InternalAppException.class)
    public void handleCreateUser_HashingFailure() throws Exception {
        when(injectedPasswordHasher.createStorableHash(PASSWORD)).thenThrow(new CannotPerformHashException("fake"));

        // Method under test
        target.handleCreateUser(input);
    }

    @Test
    public void handleCreateUser_UsernameAlreadyExists() throws Exception {
        // TODO - implement src, then this test
    }

    @Test
    public void handleCreateUser_Idempotent() throws Exception {
        // TODO - maybe this is better as integ test?
    }

}