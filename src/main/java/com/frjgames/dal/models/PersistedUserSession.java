package com.frjgames.dal.models;

import lombok.Builder;
import lombok.Data;

/**
 * Application POJO representing a user's session.
 *
 * @author fridge
 */
@Data
@Builder
public class PersistedUserSession implements AppDataModel {

    /**
     * The ID of the user who the session belongs to.
     */
    private final String userId;

    /**
     * The unique session ID of a user.
     */
    private final String sessionId;

    /**
     * Session should be considered inactive is timestamp is in the past.
     */
    private final long expiryTimestampMs;

}
