package com.frjgames.app.api.models;

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

    /**
     * A unique string provided by the caller that will be used to de-dupe retried requests.
     *
     * A good candidate for this field is the client-side request ID.
     */
    private final String idempotencyKey;
}
