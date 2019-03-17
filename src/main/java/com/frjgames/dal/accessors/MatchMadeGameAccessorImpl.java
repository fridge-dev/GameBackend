package com.frjgames.dal.accessors;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.frjgames.dal.ddb.accessors.DynamoDbAccessor;
import com.frjgames.dal.ddb.items.GameDdbItem;
import com.frjgames.dal.ddb.typeconverters.types.GameStatusType;
import com.frjgames.dal.ddb.utils.DdbExceptionTranslator;
import com.frjgames.dal.ddb.utils.DdbExpressionFactory;
import com.frjgames.dal.models.data.MatchMadeGame;
import com.frjgames.dal.models.exceptions.InvalidDataException;
import com.frjgames.dal.models.exceptions.MissingDataException;
import com.frjgames.dal.models.interfaces.MatchMadeGameAccessor;
import com.frjgames.dal.models.keys.GameIdKey;
import com.frjgames.utils.FrjConditions;
import com.frjgames.utils.TimeUtils;
import com.google.common.collect.Iterables;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import lombok.RequiredArgsConstructor;

/**
 * This class is responsible for accessing a {@link MatchMadeGame} from the DB.
 *
 * A match made game is a game that is currently unmatched, and therefore in the match-making process.
 *
 * @author fridge
 */
@RequiredArgsConstructor
public class MatchMadeGameAccessorImpl implements MatchMadeGameAccessor {

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
        item.setCreationTimeHr(TimeUtils.truncateHour(data.getCreationTimeMs()));
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
    public List<MatchMadeGame> loadAvailableGames(final long startTimestampMs) {
        long initialHashKey = TimeUtils.truncateHour(startTimestampMs);

        List<MatchMadeGame> result = queryByTime(initialHashKey, null);
        if (!result.isEmpty()) {
            return result;
        }

        // Note: Below is business logic in a DAO. This is bad. I don't have time to make it right though.

        // If the initial query was empty, it could be because the hour just flipped.
        // This 2nd query allows us to query for matches created in the last 61-120 minutes.
        return queryByTime(TimeUtils.truncateHour(initialHashKey - 1L), null);
    }

    private List<MatchMadeGame> queryByTime(final long timestampHr, @Nullable final Map<String, AttributeValue> lastEvaluatedKey) {
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

        return resultPage.getResults()
                .stream()
                .map(this::itemToDomainType)
                .collect(Collectors.toList());
    }

    /**
     * Updates the game of the provided key with a second player.
     *
     * @param userId the second player (i.e. guest) of the game.
     */
    @Override
    public void updateMatchWithGuestUser(final GameIdKey key, final String userId) {
        // Load existing match
        GameDdbItem gameItem = ddbAccessor.loadItem(key.getGameId())
                .orElseThrow(() -> new MissingDataException(String.format("Cannot update game %s which isn't present in DB.", key.getGameId())));

        // Note: Below is business logic in a DAO. This is bad. I don't have time to make it right though.

        // Validate the match
        FrjConditions.checkArg(gameItem.getStatus() == GameStatusType.UNMATCHED,
                () -> new InvalidDataException("Game state should be unmatched, but is " + gameItem.getStatus()));
        FrjConditions.checkArg(gameItem.getSecondUserId() == null,
                () -> new InvalidDataException("Game already has second user ID " + gameItem.getSecondUserId()));
        FrjConditions.checkArg(!gameItem.getFirstUserId().equals(userId),
                () -> new InvalidDataException("Game cannot be matched with first and second user as the same."));

        // Update the match
        gameItem.setSecondUserId(userId);
        gameItem.setStatus(GameStatusType.IN_PROGRESS);
        DdbExceptionTranslator.conditionalWrite(() -> ddbAccessor.saveItem(gameItem), "Race condition while setting match to in-progress.");
    }

    @Override
    public void delete(final GameIdKey key) {
        // ...but when it is supported, maybe it should be a hard delete so we don't have to filter through items when scanning.
        throw new UnsupportedOperationException("Delete game is not yet supported.");
    }

}
