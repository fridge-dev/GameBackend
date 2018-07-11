package com.mycompany.app.frj.dal.models.keys;

import lombok.Builder;
import lombok.Data;

/**
 * Key for loading a user session.
 *
 * @author alecva
 */
@Data
@Builder
public class UserSessionDataKey implements AppDataKey {

    private final String userId;

}
