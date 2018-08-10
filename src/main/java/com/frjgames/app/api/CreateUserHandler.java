package com.frjgames.app.api;

import com.frjgames.app.api.models.exceptions.InternalAppException;
import com.frjgames.app.api.models.inputs.CreateUserInput;
import com.frjgames.app.api.models.outputs.CreateUserOutput;

/**
 * Top level API for creating a new user account.
 *
 * @author fridge
 */
public interface CreateUserHandler extends ApiHandler {

    /**
     * Creates a new user.
     */
    CreateUserOutput handle(CreateUserInput input) throws InternalAppException;

}
