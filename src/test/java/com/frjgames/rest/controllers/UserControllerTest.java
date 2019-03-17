package com.frjgames.rest.controllers;

import com.frjgames.testutils.TestUtilExceptionValidator;
import javax.ws.rs.BadRequestException;
import org.junit.Test;

/**
 * TODO
 *
 * @author TODO
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