package com.mycompany.app.frj.app.api;

import com.mycompany.app.frj.app.api.exceptions.InternalAppException;
import com.mycompany.app.frj.app.api.models.CreateUserInput;
import com.mycompany.app.frj.app.api.models.CreateUserOutput;

/**
 * Top level API for creating a new user account.
 *
 * @author alecva
 */
public interface CreateUserHandler extends ApiHandler {

    /**
     * Creates a new user.
     */
    CreateUserOutput handleCreateUser(CreateUserInput input) throws InternalAppException;

}
