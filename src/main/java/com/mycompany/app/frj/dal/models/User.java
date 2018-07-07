package com.mycompany.app.frj.dal.models;

import lombok.Builder;
import lombok.Data;

/**
 * Application POJO representing a user.
 *
 * @author alecva
 */
@Data
@Builder
public class User implements AppDataModel {
    private final String userId;
    private final String username;
    private final String password;
}
