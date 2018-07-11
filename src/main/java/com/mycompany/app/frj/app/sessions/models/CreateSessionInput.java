package com.mycompany.app.frj.app.sessions.models;

import lombok.Builder;
import lombok.Data;

/**
 * Input for creating a user session.
 *
 * @author alecva
 */
@Data
@Builder
public class CreateSessionInput {

    private final String userId;

}
