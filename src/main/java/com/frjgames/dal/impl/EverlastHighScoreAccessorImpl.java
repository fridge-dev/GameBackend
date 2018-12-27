package com.frjgames.dal.impl;

import static java.util.stream.Collectors.toList;

import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.frjgames.dal.ddb.accessors.EverlastHighScoreDdbAccessor;
import com.frjgames.dal.ddb.items.EverlastHighScoreDdbItem;
import com.frjgames.dal.ddb.typeconverters.types.WorldLevelDdbType;
import com.frjgames.dal.interfaces.EverlastHighScoreAccessor;
import com.frjgames.dal.models.EverlastHighScore;
import com.frjgames.dal.models.PaginatedResult;
import com.frjgames.dal.models.keys.EverlastHighScoreDataKey;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

/**
 * TODO
 *
 * @author fridge
 */
@RequiredArgsConstructor
public class EverlastHighScoreAccessorImpl implements EverlastHighScoreAccessor {

    private final EverlastHighScoreDdbAccessor accessor;

    @Override
    public void create(final EverlastHighScore data) {
        accessor.createOrUpdateItem(domainTypeToItem(data));
    }

    private EverlastHighScoreDdbItem domainTypeToItem(final EverlastHighScore data) {
        EverlastHighScoreDdbItem item = new EverlastHighScoreDdbItem();
        item.setUserId(data.getUserId());
        item.setWorldAndLevelId(WorldLevelDdbType.builder()
                .worldId(data.getWorldId())
                .levelId(data.getLevelId())
                .build());
        item.setScore(data.getScore());
        item.setGhostData(data.getGhostData());
        item.setTimestamp(data.getTimestamp());

        return item;
    }

    @Override
    public Optional<EverlastHighScore> load(final EverlastHighScoreDataKey key) {
        WorldLevelDdbType worldAndLevelId = WorldLevelDdbType.builder()
                .worldId(key.getWorldId())
                .levelId(key.getLevelId())
                .build();

        return accessor.loadItem(key.getUserId(), worldAndLevelId)
                .map(this::itemToDomainType);
    }

    private EverlastHighScore itemToDomainType(final EverlastHighScoreDdbItem item) {
        return EverlastHighScore.builder()
                .userId(item.getUserId())
                .worldId(item.getWorldAndLevelId().getWorldId())
                .levelId(item.getWorldAndLevelId().getLevelId())
                .score(item.getScore())
                .ghostData(item.getGhostData())
                .timestamp(item.getTimestamp())
                .build();
    }

    @Override
    public List<EverlastHighScore> loadAllForUser(final String userId) {
        return accessor.loadAllHighScoresForUser(userId)
                .stream()
                .map(this::itemToDomainType)
                .collect(toList());
    }

    @Override
    public PaginatedResult<EverlastHighScore> loadForLevel(final String worldId, final String levelId) {
        return loadForLevel(worldId, levelId, null);
    }

    @Override
    public PaginatedResult<EverlastHighScore> loadForLevel(final String worldId, final String levelId, final String paginationToken) {
        WorldLevelDdbType worldAndLevelId = WorldLevelDdbType.builder()
                .worldId(worldId)
                .levelId(levelId)
                .build();

        QueryResultPage<EverlastHighScoreDdbItem> resultPage = accessor.loadHighScoresForLevel(worldAndLevelId);

        List<EverlastHighScore> collect = resultPage.getResults()
                .stream()
                .map(this::itemToDomainType)
                .collect(toList());

        resultPage.getLastEvaluatedKey();
        return null;
    }
}
