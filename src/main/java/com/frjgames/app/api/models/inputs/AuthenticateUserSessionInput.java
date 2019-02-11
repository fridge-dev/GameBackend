package com.frjgames.app.api.models.inputs;

import com.frjgames.app.api.models.interfaces.AuthenticateUserSessionHandler;
import com.frjgames.app.internal.sessions.models.SessionData;
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
