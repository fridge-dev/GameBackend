package com.frjgames.app.api.models;

import com.frjgames.app.api.CreateUserHandler;
import com.frjgames.app.sessions.models.SessionData;
import lombok.Builder;
import lombok.Data;

/**
 * Output of the {@link CreateUserHandler}.
 *
 * @author fridge
 */
@Data
@Builder
public class CreateUserOutput {

    /**
     * The unique user ID created for this user.
     */
    private final String userId;

    /**
     * The session token which can be provided with each request to prove user has signed in.
     */
    private final SessionData sessionToken;

}
