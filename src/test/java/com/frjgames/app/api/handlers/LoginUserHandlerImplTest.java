package com.frjgames.app.api.handlers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.when;

import com.frjgames.app.api.models.exceptions.IncorrectAuthException;
import com.frjgames.app.api.models.inputs.LoginUserInput;
import com.frjgames.app.api.models.outputs.LoginUserOutput;
import com.frjgames.app.internal.password.PasswordHasher;
import com.frjgames.app.internal.password.models.InvalidHashException;
import com.frjgames.app.internal.sessions.SessionManager;
import com.frjgames.app.internal.sessions.models.CreateSessionInput;
import com.frjgames.app.internal.sessions.models.SessionData;
import com.frjgames.dal.models.data.User;
import com.frjgames.dal.models.interfaces.UserAccessor;
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
 * Tests the {@link LoginUserHandlerImpl} class.
 *
 * @author fridge
 */
@RunWith(MockitoJUnitRunner.class)
public class LoginUserHandlerImplTest {

    private static final String USERNAME = "apsughapse";
    private static final String CLIENT_PASSWORD = "1948f19j19348j";
    private static final String PERSISTED_PASSWORD = "8ag7sfo8a7sdfh";

    @InjectMocks
    private LoginUserHandlerImpl subject;

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
                .username(USERNAME)
                .password(PERSISTED_PASSWORD)
                .build();
    }

    @Test
    public void handleAuthenticateUser() throws Exception {
        when(injectedUserAccessor.load(argThat(matchesUsername(USERNAME)))).thenReturn(Optional.of(mockUser));
        when(injectedPasswordHasher.matches(CLIENT_PASSWORD, PERSISTED_PASSWORD)).thenReturn(true);
        when(injectedSessionManager.createSession(argThat(matchesUserId(USERNAME)))).thenReturn(mockSession);

        LoginUserOutput output = subject.handle(newInput());

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

    private LoginUserInput newInput() {
        return LoginUserInput.builder()
                .username(USERNAME)
                .password(CLIENT_PASSWORD)
                .build();
    }

}