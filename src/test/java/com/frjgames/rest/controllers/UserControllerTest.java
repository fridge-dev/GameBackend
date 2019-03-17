package com.frjgames.rest.controllers;

import com.frjgames.testutils.TestUtilExceptionValidator;
import javax.ws.rs.BadRequestException;
import org.junit.Test;

/**
 * Tests the {@link UserController} in a brittle fashion. Good luck future me!
 *
 * @author fridge
 */
public class UserControllerTest {

    @Test
    public void test() throws Exception {
        UserController.CreateUserRestRequest request = new UserController.CreateUserRestRequest();
        request.setUsername("use12");
        request.setPassword("pasguh9as8hg");

        UserController userController = new UserController();
        userController.createUser(request);

        TestUtilExceptionValidator.assertThrows(BadRequestException.class, () -> userController.createUser(request));
    }

}