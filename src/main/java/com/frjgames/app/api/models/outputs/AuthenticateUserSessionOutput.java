package com.frjgames.app.api.models.outputs;

import com.frjgames.app.api.models.interfaces.AuthenticateUserSessionHandler;
import lombok.Builder;
import lombok.Data;

/**
 * The output to the {@link AuthenticateUserSessionHandler} API.
 *
 * @author fridge
 */
@Data
@Builder
public class AuthenticateUserSessionOutput {

    private final boolean success;

}
