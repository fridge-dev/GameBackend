package com.frjgames.dal.models.keys;

import lombok.Builder;
import lombok.Data;

/**
 * Key for loading a user.
 *
 * @author fridge
 */
@Data
@Builder
public class UserDataKey implements AppDataKey {

    private final String username;

}
