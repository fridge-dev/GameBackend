package com.frjgames.app.api.models;

import com.frjgames.app.api.AuthenticateUserHandler;
import com.frjgames.app.sessions.models.SessionData;
import lombok.Builder;
import lombok.Data;

/**
 * Output of the {@link AuthenticateUserHandler} API.
 *
 * @author fridge
 */
@Data
@Builder
public class AuthenticateUserOutput {

    private final SessionData sessionToken;

}
