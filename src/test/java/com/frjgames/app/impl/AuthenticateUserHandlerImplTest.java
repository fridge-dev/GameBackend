package com.frjgames.app.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.when;

import com.frjgames.app.api.models.exceptions.IncorrectAuthException;
import com.frjgames.app.api.models.inputs.AuthenticateUserInput;
import com.frjgames.app.api.models.outputs.AuthenticateUserOutput;
import com.frjgames.app.password.PasswordHasher;
import com.frjgames.app.password.models.InvalidHashException;
import com.frjgames.app.sessions.SessionManager;
import com.frjgames.app.sessions.models.CreateSessionInput;
import com.frjgames.app.sessions.models.SessionData;
import com.frjgames.dal.models.interfaces.UserAccessor;
import com.frjgames.dal.models.data.User;
import com.frjgames.dal.models.keys.UserDataKey;
import com.frjgames.testutils.argmatcher.TestUtilArgMatcher;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Tests the {@link AuthenticateUserHandlerImpl} class.
 *
 * @author fridge
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthenticateUserHandlerImplTest {

    private static final String USERNAME = "apsughapse";
    private static final String CLIENT_PASSWORD = "1948f19j19348j";
    private static final String USER_ID = "extcfyvgubhinj";
    private static final String PERSISTED_PASSWORD = "8ag7sfo8a7sdfh";

    @InjectMocks
    private AuthenticateUserHandlerImpl subject;

    @Mock
    private UserAccessor injectedUserAccessor;

    @Mock
    private SessionManager injectedSessionManager;

    @Mock
    private PasswordHasher injectedPasswordHasher;

    @Mock
    private SessionData mockSession;

    private User mockUser;

    @Before
    public void setup() {
        mockUser = User.builder()
                .userId(USER_ID)
                .username(USERNAME)
                .password(PERSISTED_PASSWORD)
                .build();
    }

    @Test
    public void handleAuthenticateUser() throws Exception {
        when(injectedUserAccessor.load(argThat(matchesUsername(USERNAME)))).thenReturn(Optional.of(mockUser));
        when(injectedPasswordHasher.matches(CLIENT_PASSWORD, PERSISTED_PASSWORD)).thenReturn(true);
        when(injectedSessionManager.createSession(argThat(matchesUserId(USER_ID)))).thenReturn(mockSession);

        AuthenticateUserOutput output = subject.handle(newInput());

        assertEquals(mockSession, output.getSessionToken());
    }

    @Test(expected = IncorrectAuthException.class)
    public void handleAuthenticateUser_UserNotFound() throws Exception {
        when(injectedUserAccessor.load(argThat(matchesUsername(USERNAME)))).thenReturn(Optional.empty());

        subject.handle(newInput());
    }

    @Test(expected = IncorrectAuthException.class)
    public void handleAuthenticateUser_InvalidPasswordInDatabase() throws Exception {
        when(injectedUserAccessor.load(argThat(matchesUsername(USERNAME)))).thenReturn(Optional.of(mockUser));
        when(injectedPasswordHasher.matches(CLIENT_PASSWORD, PERSISTED_PASSWORD)).thenThrow(new InvalidHashException("fake"));

        subject.handle(newInput());
    }

    @Test(expected = IncorrectAuthException.class)
    public void handleAuthenticateUser_InvalidPasswordProvided() throws Exception {
        when(injectedUserAccessor.load(argThat(matchesUsername(USERNAME)))).thenReturn(Optional.of(mockUser));
        when(injectedPasswordHasher.matches(CLIENT_PASSWORD, PERSISTED_PASSWORD)).thenReturn(false);

        subject.handle(newInput());
    }

    private TestUtilArgMatcher<UserDataKey> matchesUsername(final String username) {
        return new TestUtilArgMatcher<>((invokedArg -> invokedArg.getUsername().equals(username)));
    }

    private TestUtilArgMatcher<CreateSessionInput> matchesUserId(final String userId) {
        return new TestUtilArgMatcher<>((invokedArg -> invokedArg.getUserId().equals(userId)));
    }

    private AuthenticateUserInput newInput() {
        return AuthenticateUserInput.builder()
                .username(USERNAME)
                .password(CLIENT_PASSWORD)
                .build();
    }

}