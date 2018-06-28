package com.mycompany.app.frj.dal.models;

import lombok.Builder;
import lombok.Data;

/**
 * TODO
 *
 * @author alecva
 */
@Data
@Builder
public class User {
    private final String userId;
    private final String username;
    private final String password;
}
