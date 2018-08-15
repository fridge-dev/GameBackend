package com.frjgames.dal.models;

import lombok.Builder;
import lombok.Data;

/**
 * Application layer POJO representing a high score in the game Everlast.
 *
 * @author fridge
 */
@Data
@Builder
public class EverlastHighScore implements AppDataModel {

    /**
     * User's display name.
     */
    private final String username;

    /**
     * The unique identifier of the world.
     */
    private final int worldId;

    /**
     * The unique identifier of the level.
     */
    private final int levelId;

    /**
     * The numeric score of the high score.
     */
    private final int score;

    /**
     * The encoded data of the high score's ghost.
     */
    private final String ghostData;

    /**
     * The timestamp at which this ghost was created.
     */
    private final long timestamp;
}
