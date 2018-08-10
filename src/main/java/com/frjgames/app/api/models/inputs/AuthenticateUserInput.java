package com.frjgames.app.api.models.inputs;

import com.frjgames.app.api.AuthenticateUserHandler;
import lombok.Builder;
import lombok.Data;

/**
 * Input for the {@link AuthenticateUserHandler} API.
 *
 * @author fridge
 */
@Data
@Builder
public class AuthenticateUserInput {

    /**
     * The user who is attempting to log in.
     */
    private final String username;

    /**
     * The attempted password of the user.
     */
    private final String password;

}
