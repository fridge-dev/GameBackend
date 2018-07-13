package com.frjgames.app.api.models;

import com.frjgames.app.api.AuthenticateUserSessionHandler;
import com.frjgames.app.sessions.models.SessionData;
import lombok.Builder;
import lombok.Data;

/**
 * The input to the {@link AuthenticateUserSessionHandler} API.
 *
 * @author fridge
 */
@Data
@Builder
public class AuthenticateUserSessionInput {

    private final SessionData sessionToken;

}
