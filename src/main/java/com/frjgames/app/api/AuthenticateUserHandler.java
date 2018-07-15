package com.frjgames.app.api;

import com.frjgames.app.api.exceptions.InternalAppException;
import com.frjgames.app.api.exceptions.InvalidAuthInputException;
import com.frjgames.app.api.models.AuthenticateUserInput;
import com.frjgames.app.api.models.AuthenticateUserOutput;

/**
 * Top level API for authenticating a user at beginning of a session. Authenticating is synonymous with handling a user's login attempt.
 *
 * @author fridge
 */
public interface AuthenticateUserHandler extends ApiHandler {

    AuthenticateUserOutput handleAuthenticateUser(AuthenticateUserInput input) throws InvalidAuthInputException, InternalAppException;

}
