package com.frjgames.dal.models.interfaces;

import com.frjgames.dal.models.data.EverlastHighScore;
import com.frjgames.dal.models.data.PaginatedResult;
import com.frjgames.dal.models.keys.EverlastHighScoreDataKey;
import java.util.List;

/**
 * Accessor for high scores of the game Everlast
 *
 * @author fridge
 */
public interface EverlastHighScoreAccessor extends DataAccessor<EverlastHighScoreDataKey, EverlastHighScore> {

    List<EverlastHighScore> loadAllForUser(String userId);

    PaginatedResult<EverlastHighScore> loadForLevel(String worldId, String levelId);

    PaginatedResult<EverlastHighScore> loadForLevel(String worldId, String levelId, String paginationToken);

}
