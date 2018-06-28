package com.mycompany.app.frj.app.interfaces.models;

import lombok.Builder;
import lombok.Data;

/**
 * TODO
 *
 * @author alecva
 */
@Data
@Builder
public class CreateUserOutput {

    private final String userId;

    private final String authToken;

}
