package com.frjgames.app.api;

import com.frjgames.app.api.models.exceptions.InvalidAuthInputException;
import com.frjgames.app.api.models.inputs.AuthenticateUserSessionInput;
import com.frjgames.app.api.models.outputs.AuthenticateUserSessionOutput;

/**
 * Top level API for authenticating a user's session token. This API is designed to be used before performing actions
 * that would require a user's authentication, but without having to prompt the user for their password.
 *
 * @author fridge
 */
public interface AuthenticateUserSessionHandler extends ApiHandler {

    AuthenticateUserSessionOutput handle(AuthenticateUserSessionInput input) throws InvalidAuthInputException;

}
