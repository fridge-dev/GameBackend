package com.frjgames.app.api;

import com.frjgames.app.api.exceptions.InternalAppException;
import com.frjgames.app.api.models.CreateUserInput;
import com.frjgames.app.api.models.CreateUserOutput;

/**
 * Top level API for creating a new user account.
 *
 * @author fridge
 */
public interface CreateUserHandler extends ApiHandler {

    /**
     * Creates a new user.
     */
    CreateUserOutput handleCreateUser(CreateUserInput input) throws InternalAppException;

}
