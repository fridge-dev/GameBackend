package com.frjgames.app.impl;

import com.frjgames.app.api.AuthenticateUserHandler;
import com.frjgames.app.api.exceptions.InternalAppException;
import com.frjgames.app.api.exceptions.InvalidAuthInputException;
import com.frjgames.app.api.models.AuthenticateUserInput;
import com.frjgames.app.api.models.AuthenticateUserOutput;
import com.frjgames.app.password.PasswordHasher;
import com.frjgames.app.password.models.CannotPerformHashException;
import com.frjgames.app.password.models.InvalidHashException;
import com.frjgames.app.sessions.SessionManager;
import com.frjgames.app.sessions.models.CreateSessionInput;
import com.frjgames.app.sessions.models.SessionData;
import com.frjgames.dal.interfaces.UserAccessor;
import com.frjgames.dal.models.User;
import com.frjgames.dal.models.keys.UserDataKey;
import lombok.RequiredArgsConstructor;

/**
 * Top level API for authenticating a user.
 *
 * @author fridge
 */
@RequiredArgsConstructor
public class AuthenticateUserHandlerImpl implements AuthenticateUserHandler {

    private final UserAccessor userAccessor;

    private final SessionManager sessionManager;

    private final PasswordHasher passwordHasher;

    /**
     * Handle request by validating user's password, then creating a new session for that user.
     *
     * It will also clear any other active sessions for that user.
     */
    @Override
    public AuthenticateUserOutput handleAuthenticateUser(final AuthenticateUserInput input) throws InvalidAuthInputException, InternalAppException {
        User user = loadUser(input.getUsername());
        validateAuthentication(input, user);
        SessionData session = createNewSessionToken(user);

        return AuthenticateUserOutput.builder()
                .sessionToken(session)
                .build();
    }

    private User loadUser(final String username) throws InvalidAuthInputException {
        UserDataKey userDataKey = UserDataKey.builder()
                .username(username)
                .build();

        return userAccessor.load(userDataKey)
                .orElseThrow(() -> new InvalidAuthInputException("No such user exists."));
    }

    private void validateAuthentication(final AuthenticateUserInput input, final User user) throws InvalidAuthInputException, InternalAppException {
        boolean loginSuccess;
        try {
            loginSuccess = passwordHasher.matches(input.getPassword(), user.getPassword());
        } catch (InvalidHashException e) {
            throw new InvalidAuthInputException("User log-in params were invalid format.", e);
        } catch (CannotPerformHashException e) {
            throw new InternalAppException("Failed to perform hash to validate user's log-in params.", e);
        }

        if (!loginSuccess) {
            throw new InvalidAuthInputException("User log-in params were incorrect.");
        }
    }

    private SessionData createNewSessionToken(final User user) {
        CreateSessionInput sessionInput = CreateSessionInput.builder()
                .userId(user.getUserId())
                .build();

        return sessionManager.createSession(sessionInput);
    }

}
