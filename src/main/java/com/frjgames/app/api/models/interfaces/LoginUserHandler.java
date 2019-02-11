package com.frjgames.app.api.models.interfaces;

import com.frjgames.app.api.models.exceptions.InternalAppException;
import com.frjgames.app.api.models.exceptions.IncorrectAuthException;
import com.frjgames.app.api.models.inputs.LoginUserInput;
import com.frjgames.app.api.models.outputs.LoginUserOutput;

/**
 * Top level API for authenticating a user at beginning of a session. Authenticating is synonymous with handling a user's login attempt.
 *
 * This is the same I/O as {@link CreateUserHandler} except we expect the user to exist.
 *
 * @author fridge
 */
public interface LoginUserHandler extends ApiHandler {

    LoginUserOutput handle(LoginUserInput input) throws IncorrectAuthException, InternalAppException;

}
