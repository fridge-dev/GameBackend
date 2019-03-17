package com.frjgames.rest.controllers;

import com.frjgames.app.api.models.exceptions.DuplicateUsernameException;
import com.frjgames.app.api.models.exceptions.InternalAppException;
import com.frjgames.app.api.models.inputs.CreateUserInput;
import com.frjgames.app.api.models.interfaces.CreateUserHandler;
import com.frjgames.rest.ApiHandlerModuleSingleton;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.BeanParam;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import lombok.Data;

/**
 * The user controller. Should probably rename this resource to be UserAuth or something.
 *
 * @author fridge
 */
@Path("/users")
public class UserController {

    private final CreateUserHandler createUserHandler = ApiHandlerModuleSingleton.getInstance().getCreateUserHandler();

    /**
     * TODO move this to resource class.
     */
    @Data
    public static class CreateUserRestRequest {
        private String username;
        private String password;
    }

    @POST
    public void createUser(final @BeanParam CreateUserRestRequest request) {
        CreateUserInput apiInput = CreateUserInput.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .build();

        try {
            createUserHandler.handle(apiInput);
        } catch (InternalAppException e) {
            throw new InternalServerErrorException("Internal failure: " + e.getMessage());
        } catch (DuplicateUsernameException e) {
            throw new BadRequestException("Duplicate username: " + e.getMessage());
        }
    }
}
