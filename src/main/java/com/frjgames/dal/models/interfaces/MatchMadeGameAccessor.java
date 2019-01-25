package com.frjgames.dal.models.interfaces;

import com.frjgames.dal.models.data.MatchMadeGame;
import com.frjgames.dal.models.data.PaginatedResult;
import com.frjgames.dal.models.exceptions.DataAccessLayerException;
import com.frjgames.dal.models.keys.GameIdKey;
import java.util.Optional;

/**
 * This abstracts away the underlying match making game DB.
 *
 * @author fridge
 */
public interface MatchMadeGameAccessor extends DataAccessor<GameIdKey, MatchMadeGame> {

    /**
     * Load the game via the name index.
     */
    Optional<MatchMadeGame> loadByName(final String gameName) throws DataAccessLayerException;

    /**
     * Load available games in descending order. This API supports pagination.
     */
    PaginatedResult<MatchMadeGame> loadAvailableGames(final long startTimestampMs) throws DataAccessLayerException;

    /**
     * Load available games in descending order. This API supports pagination.
     */
    PaginatedResult<MatchMadeGame> loadAvailableGames(final String paginationToken) throws DataAccessLayerException;

}
