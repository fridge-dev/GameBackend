package com.mycompany.app.frj.app.api.models;

import com.mycompany.app.frj.app.sessions.models.SessionData;
import lombok.Builder;
import lombok.Data;

/**
 * Output of the {@link com.mycompany.app.frj.app.api.CreateUserHandler}.
 *
 * @author alecva
 */
@Data
@Builder
public class CreateUserOutput {

    private final String userId;

    private final SessionData sessionToken;

}
