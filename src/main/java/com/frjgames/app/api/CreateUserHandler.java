package com.frjgames.app.api;

import com.frjgames.app.api.models.exceptions.DuplicateUsernameException;
import com.frjgames.app.api.models.exceptions.InternalAppException;
import com.frjgames.app.api.models.inputs.CreateUserInput;
import com.frjgames.app.api.models.outputs.CreateUserOutput;

/**
 * Top level API for creating a new user account.
 *
 * This is the same I/O as the {@link AuthenticateUserHandler} except we expect the user to NOT exist.
 *
 * @author fridge
 */
public interface CreateUserHandler extends ApiHandler {

    /**
     * Creates a new user.
     */
    CreateUserOutput handle(CreateUserInput input) throws InternalAppException, DuplicateUsernameException;

}
