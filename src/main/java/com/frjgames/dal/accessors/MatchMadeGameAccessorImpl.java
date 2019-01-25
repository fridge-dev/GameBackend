package com.frjgames.dal.accessors;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frjgames.dal.ddb.accessors.DynamoDbAccessor;
import com.frjgames.dal.ddb.items.GameDdbItem;
import com.frjgames.dal.ddb.typeconverters.types.GameStatusType;
import com.frjgames.dal.ddb.utils.DdbExceptionTranslator;
import com.frjgames.dal.ddb.utils.DdbExpressionFactory;
import com.frjgames.dal.ddb.utils.DdbPaginationMapper;
import com.frjgames.dal.models.data.MatchMadeGame;
import com.frjgames.dal.models.data.PaginatedResult;
import com.frjgames.dal.models.exceptions.DataSerializationException;
import com.frjgames.dal.models.interfaces.MatchMadeGameAccessor;
import com.frjgames.dal.models.keys.GameIdKey;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * This class is responsible for accessing a {@link MatchMadeGame} from the DB.
 *
 * A match made game is a game that is currently unmatched, and therefore in the match-making process.
 *
 * @author fridge
 */
@RequiredArgsConstructor
public class MatchMadeGameAccessorImpl implements MatchMadeGameAccessor {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class GamePaginationToken {
        private long timestampHr;
        private String internalPaginationToken;
    }

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final DynamoDbAccessor<GameDdbItem> ddbAccessor;

    /**
     * Create the match made game, with check if the game ID doesn't already exist.
     */
    @Override
    public void create(final MatchMadeGame data) {
        GameDdbItem item = new GameDdbItem();
        item.setGameId(data.getGameId());
        item.setGameName(data.getGameName());
        item.setFirstUserId(data.getHostUserId());
        item.setCreationTimeMs(data.getCreationTimeMs());
        item.setCreationTimeHr(GameTimestampConverter.truncateHour(data.getCreationTimeMs()));
        item.setStatus(GameStatusType.UNMATCHED);

        DdbExceptionTranslator.conditionalWrite(() -> ddbAccessor.saveItem(item), "Cannot create game because it already exists.");
    }

    /**
     * Load the game by ID.
     */
    @Override
    public Optional<MatchMadeGame> load(final GameIdKey key) {
        return ddbAccessor.loadItem(key.getGameId())
                .map(this::itemToDomainType);
    }

    private MatchMadeGame itemToDomainType(final GameDdbItem item) {
        return MatchMadeGame.builder()
                .gameId(item.getGameId())
                .gameName(item.getGameName())
                .hostUserId(item.getFirstUserId())
                .creationTimeMs(item.getCreationTimeMs())
                .isGameUnmatched(item.getStatus() == GameStatusType.UNMATCHED)
                .build();
    }

    /**
     * Load the game by name.
     */
    @Override
    public Optional<MatchMadeGame> loadByName(final String gameName) {
        return queryFirstByNameIndex(gameName)
                .map(this::itemToDomainType);
    }

    private Optional<GameDdbItem> queryFirstByNameIndex(final String gameName) {
        GameDdbItem key = new GameDdbItem();
        key.setGameName(gameName);

        DynamoDBQueryExpression<GameDdbItem> query = new DynamoDBQueryExpression<GameDdbItem>()
                .withIndexName(GameDdbItem.GSI_GAME_NAME)
                .withHashKeyValues(key)
                .withConsistentRead(false)
                // Filter only unmatched games.
                .withQueryFilterEntry(GameDdbItem.COL_STATUS, DdbExpressionFactory.newConditionColumnEquals(GameStatusType.UNMATCHED.name()))
                .withLimit(1);

        QueryResultPage<GameDdbItem> resultPage = ddbAccessor.querySinglePage(query);
        List<GameDdbItem> results = resultPage.getResults();

        // Just return the first, if it exists. This will not support multiple unmatched games with the same name.
        // This use case will be rare, so we won't design for it yet.
        return Optional.ofNullable(Iterables.getFirst(results, null));
    }

    /**
     * @inheritDoc
     */
    @Override
    public PaginatedResult<MatchMadeGame> loadAvailableGames(final long startTimestampMs) {
        long initialHashKey = GameTimestampConverter.truncateHour(startTimestampMs);

        PaginatedResult<MatchMadeGame> result = queryByTime(initialHashKey, null);
        if (!result.getResults().isEmpty()) {
            return result;
        }

        // If the initial query was empty, it could be because the hour just flipped.
        // This 2nd query allows us to query for matches created in the last 61-120 minutes.
        return queryByTime(GameTimestampConverter.truncateHour(initialHashKey - 1L), null);
    }

    private PaginatedResult<MatchMadeGame> queryByTime(final long timestampHr, @Nullable final Map<String, AttributeValue> lastEvaluatedKey) {
        GameDdbItem key = new GameDdbItem();
        key.setCreationTimeHr(timestampHr);

        DynamoDBQueryExpression<GameDdbItem> query = new DynamoDBQueryExpression<GameDdbItem>()
                .withIndexName(GameDdbItem.GSI_CREATION_TIME)
                .withHashKeyValues(key)
                .withConsistentRead(false)
                .withScanIndexForward(false)
                .withQueryFilterEntry(GameDdbItem.COL_STATUS, DdbExpressionFactory.newConditionColumnEquals(GameStatusType.UNMATCHED.name()))
                .withExclusiveStartKey(lastEvaluatedKey);

        QueryResultPage<GameDdbItem> resultPage = ddbAccessor.querySinglePage(query);

        // Since we may use a different hash key than the one requested by caller, we need
        // to encode this information in the pagination token for the next request.
        PaginatedResult<MatchMadeGame> paginatedResult = DdbPaginationMapper.makePaginatedResult(resultPage, this::itemToDomainType);
        String externalToken = paginatedResult.getPaginationToken()
                .map(internalToken -> encodeHashKeyIntoPaginationToken(timestampHr, internalToken))
                .orElse(null);

        return new PaginatedResult<>(paginatedResult.getResults(), externalToken);
    }

    /**
     * @inheritDoc
     */
    @Override
    public PaginatedResult<MatchMadeGame> loadAvailableGames(final String paginationToken) {
        GamePaginationToken token = decodePaginationToken(paginationToken);
        Map<String, AttributeValue> lastEvaluatedKey = DdbPaginationMapper.extractLastEvaluatedKey(token.getInternalPaginationToken());

        return queryByTime(token.getTimestampHr(), lastEvaluatedKey);
    }

    private String encodeHashKeyIntoPaginationToken(final long timestampHr, final String paginationToken) {
        try {
            return OBJECT_MAPPER.writeValueAsString(new GamePaginationToken(timestampHr, paginationToken));
        } catch (JsonProcessingException e) {
            throw new DataSerializationException("Failed to encode pagination token.", e);
        }
    }

    private GamePaginationToken decodePaginationToken(final String externalPaginationToken) {
        Preconditions.checkArgument(StringUtils.isNotBlank(externalPaginationToken), "Null pagination token.");

        try {
            return OBJECT_MAPPER.readValue(externalPaginationToken, GamePaginationToken.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid pagination token provided. Cannot deserialize.", e);
        }
    }
}
