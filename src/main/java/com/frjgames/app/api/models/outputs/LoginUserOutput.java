package com.frjgames.app.api.models.outputs;

import com.frjgames.app.api.models.interfaces.LoginUserHandler;
import com.frjgames.app.internal.sessions.models.SessionData;
import lombok.Builder;
import lombok.Data;

/**
 * Output of the {@link LoginUserHandler} API.
 *
 * @author fridge
 */
@Data
@Builder
public class LoginUserOutput {

    private final SessionData sessionToken;

}
