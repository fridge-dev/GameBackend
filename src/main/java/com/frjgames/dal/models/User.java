package com.frjgames.dal.models;

import lombok.Builder;
import lombok.Data;

/**
 * Application POJO representing a user.
 *
 * @author fridge
 */
@Data
@Builder
public class User implements AppDataModel {
    private final String userId;
    private final String username;
    private final String password;
}
