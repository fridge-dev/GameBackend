package com.mycompany.app.frj.app.interfaces.models;

import com.mycompany.app.frj.app.sessions.models.SessionData;
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

    private final SessionData sessionToken;

}
