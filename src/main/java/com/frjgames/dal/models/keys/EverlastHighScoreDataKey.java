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
     * The unique identifier of the user.
     */
    private final String userId;

    /**
     * The unique identifier of the world.
     */
    private final String worldId;

    /**
     * The unique identifier of the level.
     */
    private final String levelId;

}
