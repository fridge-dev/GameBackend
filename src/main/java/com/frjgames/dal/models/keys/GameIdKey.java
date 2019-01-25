package com.frjgames.dal.models.keys;

import lombok.Builder;
import lombok.Data;

/**
 * Simple key containing the game ID.
 *
 * @author fridge
 */
@Data
@Builder
public class GameIdKey implements AppDataKey {

    private final String gameId;

}
