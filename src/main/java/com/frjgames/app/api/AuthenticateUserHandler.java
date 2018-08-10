package com.frjgames.app.api;

import com.frjgames.app.api.models.exceptions.InternalAppException;
import com.frjgames.app.api.models.exceptions.InvalidAuthInputException;
import com.frjgames.app.api.models.inputs.AuthenticateUserInput;
import com.frjgames.app.api.models.outputs.AuthenticateUserOutput;

/**
 * Top level API for authenticating a user at beginning of a session. Authenticating is synonymous with handling a user's login attempt.
 *
 * @author fridge
 */
public interface AuthenticateUserHandler extends ApiHandler {

    AuthenticateUserOutput handle(AuthenticateUserInput input) throws InvalidAuthInputException, InternalAppException;

}
