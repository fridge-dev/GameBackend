package com.frjgames.app.api.models.inputs;

import com.frjgames.app.api.models.interfaces.LoginUserHandler;
import lombok.Builder;
import lombok.Data;

/**
 * Input for the {@link LoginUserHandler} API.
 *
 * @author fridge
 */
@Data
@Builder
public class LoginUserInput {

    /**
     * The user who is attempting to log in.
     */
    private final String username;

    /**
     * The attempted password of the user.
     */
    private final String password;

}
