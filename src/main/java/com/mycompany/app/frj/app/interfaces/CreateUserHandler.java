package com.mycompany.app.frj.app.interfaces;

import com.mycompany.app.frj.app.interfaces.models.CreateUserInput;
import com.mycompany.app.frj.app.interfaces.models.CreateUserOutput;

/**
 * TODO
 *
 * @author alecva
 */
public interface CreateUserHandler {

    CreateUserOutput handleCreateUser(CreateUserInput input);

}
