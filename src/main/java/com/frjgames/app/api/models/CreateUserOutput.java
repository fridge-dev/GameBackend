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

    private final String userId;

    private final SessionData sessionToken;

}
