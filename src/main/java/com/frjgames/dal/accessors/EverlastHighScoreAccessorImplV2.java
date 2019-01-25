package com.frjgames.dal.accessors;

import static java.util.stream.Collectors.toList;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.frjgames.dal.ddb.accessors.DynamoDbAccessor;
import com.frjgames.dal.ddb.items.EverlastHighScoreDdbItem;
import com.frjgames.dal.ddb.typeconverters.types.WorldLevelDdbType;
import com.frjgames.dal.ddb.utils.DdbPaginationMapper;
import com.frjgames.dal.models.interfaces.EverlastHighScoreAccessor;
import com.frjgames.dal.models.data.EverlastHighScore;
import com.frjgames.dal.models.data.PaginatedResult;
import com.frjgames.dal.models.keys.EverlastHighScoreDataKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nullable;
import lombok.RequiredArgsConstructor;

/**
 * This class is responsible for accessing data from the EverlastHighScore data store.
 *
 * @author fridge
 */
@RequiredArgsConstructor
public class EverlastHighScoreAccessorImplV2 implements EverlastHighScoreAccessor {

    private final DynamoDbAccessor<EverlastHighScoreDdbItem> ddbAccessor;

    /**
     * Save or overwrite a new high score for a user.
     */
    @Override
    public void create(final EverlastHighScore data) {
        ddbAccessor.saveItem(domainTypeToItem(data));
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

    /**
     * Load the user's high score for a specific level.
     */
    @Override
    public Optional<EverlastHighScore> load(final EverlastHighScoreDataKey key) {
        WorldLevelDdbType worldAndLevelId = WorldLevelDdbType.builder()
                .worldId(key.getWorldId())
                .levelId(key.getLevelId())
                .build();

        return ddbAccessor.loadItem(key.getUserId(), worldAndLevelId)
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

    /**
     * Get all of the user's high scores, one for each level.
     */
    @Override
    public List<EverlastHighScore> loadAllForUser(final String userId) {
        PaginatedQueryList<EverlastHighScoreDdbItem> allItems = loadAllHighScoresForUser(userId);

        // Copy to new array forces that all results are paginated through within this method.
        return new ArrayList<>(allItems)
                .stream()
                .map(this::itemToDomainType)
                .collect(toList());
    }

    /**
     * Get all of the user's high scores.
     */
    private PaginatedQueryList<EverlastHighScoreDdbItem> loadAllHighScoresForUser(final String userId) {
        EverlastHighScoreDdbItem key = new EverlastHighScoreDdbItem();
        key.setUserId(userId);

        DynamoDBQueryExpression<EverlastHighScoreDdbItem> query = new DynamoDBQueryExpression<EverlastHighScoreDdbItem>()
                .withHashKeyValues(key)
                .withScanIndexForward(true);

        return ddbAccessor.queryAllItems(query);
    }

    /**
     * Get the top high scores for a specific level. High scores are from distinct users. This API supports pagination.
     *
     * @see #loadForLevel(String, String, String)
     */
    @Override
    public PaginatedResult<EverlastHighScore> loadForLevel(final String worldId, final String levelId) {
        return loadForLevel(worldId, levelId, null);
    }

    /**
     * Paginated version of {@link #loadForLevel(String, String)}. Use the pagination token from {@link PaginatedResult#paginationToken}
     */
    @Override
    public PaginatedResult<EverlastHighScore> loadForLevel(final String worldId, final String levelId, @Nullable final String paginationToken) {
        WorldLevelDdbType worldAndLevelId = WorldLevelDdbType.builder()
                .worldId(worldId)
                .levelId(levelId)
                .build();

        Map<String, AttributeValue> exclusiveStartKey = paginationToken == null
                ? null
                : DdbPaginationMapper.extractLastEvaluatedKey(paginationToken);
        QueryResultPage<EverlastHighScoreDdbItem> resultPage = loadHighScoresForLevel(worldAndLevelId, exclusiveStartKey);

        return DdbPaginationMapper.makePaginatedResult(resultPage, this::itemToDomainType);
    }

    private QueryResultPage<EverlastHighScoreDdbItem> loadHighScoresForLevel(
            final WorldLevelDdbType worldLevelId,
            final Map<String, AttributeValue> exclusiveStartKey
    ) {
        EverlastHighScoreDdbItem key = new EverlastHighScoreDdbItem();
        key.setWorldAndLevelId(worldLevelId);

        DynamoDBQueryExpression<EverlastHighScoreDdbItem> query = new DynamoDBQueryExpression<EverlastHighScoreDdbItem>()
                .withIndexName(EverlastHighScoreDdbItem.INDEX_LEVEL_ID_SCORE)
                .withHashKeyValues(key)
                .withConsistentRead(false) // GSI can't read consistently
                .withScanIndexForward(false)
                .withExclusiveStartKey(exclusiveStartKey);

        return ddbAccessor.querySinglePage(query);
    }
}
