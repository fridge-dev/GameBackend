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
public class UserSessionDataAccessKey implements AppDataAccessKey {

    private final String userId;

}
