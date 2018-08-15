package com.frjgames.dal.models.keys;

import lombok.Builder;
import lombok.Data;

/**
 * Key for loading an Everlast high score.
 *
 * @author fridge
 */
@Data
@Builder
public class EverlastHighScoreDataKey implements AppDataKey {

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

}
