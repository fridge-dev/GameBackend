package com.frjgames.app.impl;

import com.frjgames.app.api.models.outputs.CreateUserOutput;
import com.frjgames.app.api.CreateUserHandler;
import com.frjgames.app.api.models.exceptions.InternalAppException;
import com.frjgames.app.api.models.inputs.CreateUserInput;
import com.frjgames.app.password.PasswordHasher;
import com.frjgames.app.password.models.CannotPerformHashException;
import com.frjgames.app.password.models.InvalidHashException;
import com.frjgames.app.sessions.SessionManager;
import com.frjgames.app.sessions.models.CreateSessionInput;
import com.frjgames.app.sessions.models.SessionData;
import com.frjgames.app.utils.UniqueIdUtils;
import com.frjgames.dal.models.interfaces.UserAccessor;
import com.frjgames.dal.models.data.User;
import com.frjgames.dal.models.keys.UserDataKey;
import lombok.RequiredArgsConstructor;

/**
 * Top level class for creating a new user account.
 *
 * @author fridge
 */
@RequiredArgsConstructor
public class CreateUserHandlerImpl implements CreateUserHandler {

    private final UniqueIdUtils uniqueIdUtils;

    private final UserAccessor userAccessor;

    private final SessionManager sessionManager;

    private final PasswordHasher passwordHasher;

    /**
     * Handle request by creating user, then creating a session for that user.
     */
    public CreateUserOutput handle(final CreateUserInput input) throws InternalAppException {
        String username = input.getUsername();
        String password = input.getPassword();

        createUser(username, password);
        User user = loadOurOwnWrite(username);
        SessionData token = createSessionToken(user);

        return CreateUserOutput.builder()
                .userId(user.getUserId())
                .sessionToken(token)
                .build();
    }

    private void createUser(final String username, final String password) throws InternalAppException {
        String userId = uniqueIdUtils.newUserId();

        String hashedPassword;
        try {
            hashedPassword = passwordHasher.createStorableHash(password);
        } catch (InvalidHashException | CannotPerformHashException e) {
            throw new InternalAppException("Failed to create a storable hash of the new user's password.", e);
        }

        User user = User.builder()
                .userId(userId)
                .username(username)
                .password(hashedPassword)
                .build();

        // Does not handle case when username already exists :0
        userAccessor.create(user);
    }

    /**
     * Read-our-own-write to ensure the DB is in consistent state (GSI updated).
     */
    private User loadOurOwnWrite(final String username) {
        UserDataKey key = UserDataKey.builder()
                .username(username)
                .build();

        // Should add retries instead.
        return userAccessor.load(key).orElseThrow(() -> new IllegalStateException("nope"));
    }

    private SessionData createSessionToken(final User user) {
        CreateSessionInput sessionInput = CreateSessionInput.builder()
                .userId(user.getUserId())
                .build();

        return sessionManager.createSession(sessionInput);
    }
}
