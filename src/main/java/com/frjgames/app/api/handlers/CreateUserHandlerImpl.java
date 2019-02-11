package com.frjgames.app.api.handlers;

import com.frjgames.app.api.models.interfaces.CreateUserHandler;
import com.frjgames.app.api.models.exceptions.DuplicateUsernameException;
import com.frjgames.app.api.models.exceptions.InternalAppException;
import com.frjgames.app.api.models.inputs.CreateUserInput;
import com.frjgames.app.api.models.outputs.CreateUserOutput;
import com.frjgames.app.internal.password.PasswordHasher;
import com.frjgames.app.internal.password.models.CannotPerformHashException;
import com.frjgames.app.internal.password.models.InvalidHashException;
import com.frjgames.app.internal.sessions.SessionManager;
import com.frjgames.app.internal.sessions.models.CreateSessionInput;
import com.frjgames.app.internal.sessions.models.SessionData;
import com.frjgames.dal.models.data.User;
import com.frjgames.dal.models.exceptions.ConditionalWriteException;
import com.frjgames.dal.models.interfaces.UserAccessor;
import lombok.RequiredArgsConstructor;

/**
 * Top level class for creating a new user account.
 *
 * @author fridge
 */
@RequiredArgsConstructor
public class CreateUserHandlerImpl implements CreateUserHandler {

    private final UserAccessor userAccessor;

    private final SessionManager sessionManager;

    private final PasswordHasher passwordHasher;

    /**
     * Handle request by creating user, then creating a session for that user.
     */
    public CreateUserOutput handle(final CreateUserInput input) throws InternalAppException, DuplicateUsernameException {
        String username = input.getUsername();
        String password = input.getPassword();

        User user = createUser(username, password);

        // After user is created, we must not let any other part of the API fail, because client's retry
        // will result in a DuplicateUsernameException.
        SessionData token;
        try {
            token = createSessionToken(user);
        } catch (RuntimeException e) {
            // TODO log
            token = null;
        }

        return CreateUserOutput.builder()
                .sessionToken(token)
                .build();
    }

    private User createUser(final String username, final String password) throws InternalAppException, DuplicateUsernameException {
        String hashedPassword;
        try {
            hashedPassword = passwordHasher.createStorableHash(password);
        } catch (InvalidHashException | CannotPerformHashException e) {
            throw new InternalAppException("Failed to create a storable hash of the new user's password.", e);
        }

        User user = User.builder()
                .username(username)
                .password(hashedPassword)
                .build();

        try {
            userAccessor.create(user);
        } catch (ConditionalWriteException e) {
            throw new DuplicateUsernameException("Username is already taken.", e);
        }

        return user;
    }

    private SessionData createSessionToken(final User user) {
        // Username <-> UserID
        CreateSessionInput sessionInput = CreateSessionInput.builder()
                .userId(user.getUsername())
                .build();

        return sessionManager.createSession(sessionInput);
    }
}
