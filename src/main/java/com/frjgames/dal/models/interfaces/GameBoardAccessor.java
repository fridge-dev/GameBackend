package com.frjgames.dal.models.interfaces;

import com.frjgames.dal.models.data.GameBoard;
import com.frjgames.dal.models.exceptions.DataAccessLayerException;
import com.frjgames.dal.models.keys.GameIdKey;

/**
 * Abstracts away the underlying game board data store.
 *
 * @author fridge
 */
public interface GameBoardAccessor extends DataAccessor<GameIdKey, GameBoard> {

    /**
     * Update the board with the latest move.
     */
    void updateBoardWithMove(GameBoard gameBoard) throws DataAccessLayerException;

}
