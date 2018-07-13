package com.frjgames.dal.models.keys;

import lombok.Builder;
import lombok.Data;

/**
 * Key for loading a user session.
 *
 * @author fridge
 */
@Data
@Builder
public class UserSessionDataKey implements AppDataKey {

    private final String userId;

}
