package com.frjgames.dal.models.interfaces;

import com.frjgames.dal.models.data.Game;
import com.frjgames.dal.models.enums.GameStatusResultType;
import com.frjgames.dal.models.exceptions.DataAccessLayerException;
import com.frjgames.dal.models.keys.GameIdKey;

/**
 * This abstracts away the underlying Games DB.
 *
 * @author fridge
 */
public interface GameAccessor extends DataAccessor<GameIdKey, Game> {

    /**
     * Update the match once it's been completed.
     */
    void updateGameCompleted(GameIdKey key, GameStatusResultType statusType, String winnerUserId) throws DataAccessLayerException;

}
