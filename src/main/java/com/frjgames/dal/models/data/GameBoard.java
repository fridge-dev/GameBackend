package com.frjgames.dal.models.data;

import com.frjgames.dal.models.enums.PlayerEnum;
import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 * The model of a game board.
 *
 * @author fridge
 */
@Data
@Builder(toBuilder = true)
public class GameBoard implements AppDataModel {

    /**
     * Unique ID of game
     */
    private final String gameId;

    /**
     * The nested, ultimate game board. [0,9) indexes.
     */
    private final List<List<PlayerEnum>> gameBoardState;

    /**
     * Which player gets next move.
     */
    private final PlayerEnum nextMove;

    /**
     * Within which board the next move must be.
     */
    private final int nextBoardIndex;

    /**
     * Used for updating a board. Just pass back in the value that was loaded from DB.
     */
    private final String boardVersion;

}
