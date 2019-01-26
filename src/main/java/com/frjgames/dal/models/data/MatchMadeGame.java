package com.frjgames.dal.models.data;

import lombok.Builder;
import lombok.Data;

/**
 * The model of a game that is currently unmatched, and therefore in the match-making process.
 *
 * @see Game
 *
 * @author fridge
 */
@Data
@Builder(toBuilder = true)
public class MatchMadeGame implements AppDataModel {

    private final String gameId;

    private final String gameName;

    private final String hostUserId;

    private final long creationTimeMs;

    // Note to future self: Should there be filtering logic in the related #load method which just treats this as an item not existing.
    private final boolean isGameUnmatched;

}
