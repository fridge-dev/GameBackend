package com.frjgames.dal.models.data;

import lombok.Builder;
import lombok.Data;

/**
 * Application POJO representing a user.
 *
 * @author fridge
 */
@Data
@Builder(toBuilder = true)
public class User implements AppDataModel {
    private final String userId;
    private final String username;
    private final String password;
}
