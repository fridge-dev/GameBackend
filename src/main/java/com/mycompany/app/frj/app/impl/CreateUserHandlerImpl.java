package com.mycompany.app.frj.app.impl;

import com.mycompany.app.frj.app.auth.AuthTokenGenerator;
import com.mycompany.app.frj.app.interfaces.CreateUserHandler;
import com.mycompany.app.frj.app.interfaces.models.CreateUserInput;
import com.mycompany.app.frj.app.interfaces.models.CreateUserOutput;
import com.mycompany.app.frj.app.password.PasswordHasher;
import com.mycompany.app.frj.app.password.models.CannotPerformHashException;
import com.mycompany.app.frj.app.password.models.InvalidHashException;
import com.mycompany.app.frj.dal.interfaces.UserAccessor;
import com.mycompany.app.frj.dal.models.User;
import com.mycompany.app.frj.dal.models.keys.UserDataAccessKey;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

/**
 * TODO
 *
 * @author alecva
 */
@RequiredArgsConstructor
public class CreateUserHandlerImpl implements CreateUserHandler {

    private final UserAccessor userAccessor;

    private final AuthTokenGenerator authTokenGenerator;

    private final PasswordHasher passwordHasher;

    /**
     * TODO
     */
    public CreateUserOutput handleCreateUser(final CreateUserInput input) {
        String username = input.getUsername();
        String password = input.getPassword();

        createUser(username, password);
        User user = loadOurOwnWrite(username);
        String token = authTokenGenerator.generateToken(user);

        return CreateUserOutput.builder()
                .userId(user.getUserId())
                .authToken(token)
                .build();
    }

    private void createUser(final String username, final String password) {
        String userId = newUserId();

        String hashedPassword = null;
        try {
            hashedPassword = passwordHasher.createStorableHash(password);
        } catch (InvalidHashException e) {
            // TODO
            e.printStackTrace();
        } catch (CannotPerformHashException e) {
            // TODO
            e.printStackTrace();
        }

        User user = User.builder()
                .userId(userId)
                .username(username)
                .password(hashedPassword)
                .build();

        userAccessor.create(user);
    }

    private String newUserId() {
        return UUID.randomUUID().toString();
    }

    /**
     * Read-our-own-write to ensure the DB is in consistent state (GSI updated).
     */
    private User loadOurOwnWrite(final String username) {
        UserDataAccessKey key = UserDataAccessKey.builder()
                .username(username)
                .build();

        return userAccessor.load(key).orElseThrow(() -> new IllegalStateException("nope"));
    }
}
