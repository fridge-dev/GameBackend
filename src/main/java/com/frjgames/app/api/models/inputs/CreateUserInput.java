package com.frjgames.app.api.models.inputs;

import com.frjgames.app.api.CreateUserHandler;
import lombok.Builder;
import lombok.Data;

/**
 * Input to the {@link CreateUserHandler}.
 *
 * @author fridge
 */
@Data
@Builder
public class CreateUserInput {

    /**
     * Username provided by client.
     */
    private final String username;

    /**
     * Hashed password provided by client.
     */
    private final String password;

}
