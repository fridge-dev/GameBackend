package com.mycompany.app.frj.dal.models.keys;

import lombok.Builder;
import lombok.Data;

/**
 * Key for loading a user. Currently, only username is supported.
 *
 * @author alecva
 */
@Data
@Builder
public class UserDataAccessKey implements AppDataAccessKey {

    private final String username;

}
