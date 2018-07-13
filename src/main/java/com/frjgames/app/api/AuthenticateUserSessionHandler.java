package com.frjgames.app.api;

import com.frjgames.app.api.exceptions.InvalidAuthInputException;
import com.frjgames.app.api.models.AuthenticateUserSessionInput;
import com.frjgames.app.api.models.AuthenticateUserSessionOutput;

/**
 * Top level API for authenticating a user's session token. This API is designed to be used before performing actions
 * that would require a user's authentication, but without having to prompt the user for their password.
 *
 * @author fridge
 */
public interface AuthenticateUserSessionHandler {

    AuthenticateUserSessionOutput handleAuthenticateUserSession(AuthenticateUserSessionInput input) throws InvalidAuthInputException;

}
