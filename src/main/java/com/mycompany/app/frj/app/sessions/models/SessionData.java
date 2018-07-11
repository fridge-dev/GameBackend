package com.mycompany.app.frj.app.sessions.models;

import lombok.Builder;
import lombok.Data;

/**
 * Output for creating a user session.
 *
 * The data in this object is meant to be sent to the end user.
 *
 * @author alecva
 */
@Data
@Builder
public class SessionData {

    /**
     * Encoded session ID that is safe to be returned to the end user.
     */
    private final String publicSessionToken;

    /**
     * User to whom the session belongs
     */
    private final String userId;

    /**
     * Timestamp at which this session ID will no longer be honored.
     */
    private final long expirationTimestampMs;

}
