package com.mycompany.app.frj.app.impl;

import com.mycompany.app.frj.app.api.CreateUserHandler;
import com.mycompany.app.frj.app.api.exceptions.InternalAppException;
import com.mycompany.app.frj.app.api.models.CreateUserInput;
import com.mycompany.app.frj.app.api.models.CreateUserOutput;
import com.mycompany.app.frj.app.password.PasswordHasher;
import com.mycompany.app.frj.app.password.models.CannotPerformHashException;
import com.mycompany.app.frj.app.password.models.InvalidHashException;
import com.mycompany.app.frj.app.sessions.SessionManager;
import com.mycompany.app.frj.app.sessions.models.CreateSessionInput;
import com.mycompany.app.frj.app.sessions.models.SessionData;
import com.mycompany.app.frj.app.utils.UniqueIdUtils;
import com.mycompany.app.frj.dal.interfaces.UserAccessor;
import com.mycompany.app.frj.dal.models.User;
import com.mycompany.app.frj.dal.models.keys.UserDataKey;
import lombok.RequiredArgsConstructor;

/**
 * Top level class for creating a new user account.
 *
 * @author alecva
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
    public CreateUserOutput handleCreateUser(final CreateUserInput input) throws InternalAppException {
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
