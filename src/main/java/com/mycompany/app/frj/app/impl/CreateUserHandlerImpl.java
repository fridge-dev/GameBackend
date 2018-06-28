package com.mycompany.app.frj.app.impl;

import com.mycompany.app.frj.app.auth.AuthTokenGenerator;
import com.mycompany.app.frj.app.interfaces.CreateUserHandler;
import com.mycompany.app.frj.app.interfaces.models.CreateUserInput;
import com.mycompany.app.frj.app.interfaces.models.CreateUserOutput;
import com.mycompany.app.frj.dal.interfaces.UserAccessor;
import com.mycompany.app.frj.dal.models.User;
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

    /**
     * TODO
     */
    public CreateUserOutput handleCreateUser(final CreateUserInput input) {
        String username = input.getUsername();
        String password = input.getPassword();

        createUser(username, password);
        User user = loadUser(username);
        String token = authTokenGenerator.generateToken(user);

        return CreateUserOutput.builder()
                .userId(user.getUserId())
                .authToken(token)
                .build();
    }

    private void createUser(final String username, final String password) {
        User user = User.builder()
                .username(username)
                .password(password)
                .build();

        userAccessor.createNewUser(user);

    }

    /**
     * Read-our-own-write to ensure the DB is in consistent state (GSI updated), and also to retrieve the created user ID.
     */
    private User loadUser(final String username) {
        return userAccessor.getUserByUsername(username).orElseThrow(() -> new IllegalStateException("nope"));
    }
}
