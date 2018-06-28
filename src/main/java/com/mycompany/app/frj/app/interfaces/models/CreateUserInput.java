package com.mycompany.app.frj.app.interfaces.models;

import lombok.Data;

/**
 * TODO
 *
 * @author alecva
 */
@Data
public class CreateUserInput {

    /**
     * Username provided by client.
     */
    private String username;

    /**
     * Hashed password provided by client.
     */
    private String password;
}
