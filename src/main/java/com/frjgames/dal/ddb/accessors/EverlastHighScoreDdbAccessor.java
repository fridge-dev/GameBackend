package com.frjgames.dal.ddb.accessors;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.frjgames.dal.ddb.items.EverlastHighScoreDdbItem;
import com.frjgames.dal.ddb.typeconverters.types.WorldLevelDdbType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This class is responsible for accessing items from the EverlastHighScore DynamoDB table.
 *
 * @author fridge
 */
public class EverlastHighScoreDdbAccessor extends BaseDynamoDbAccessor<EverlastHighScoreDdbItem> {

    public EverlastHighScoreDdbAccessor(final DynamoDBMapper dbMapper) {
        super(dbMapper, EverlastHighScoreDdbItem.class);
    }

    /**
     * Save or overwrite a new high score for a user.
     */
    public void createOrUpdateItem(final EverlastHighScoreDdbItem item) {
        super.saveItem(item);
    }

    /**
     * Load the user's high score for a specific level.
     */
    public Optional<EverlastHighScoreDdbItem> loadItem(final String userId, final WorldLevelDdbType worldLevelId) {
        return super.loadItem(userId, worldLevelId);
    }

    /**
     * Get all of the user's high scores.
     */
    public List<EverlastHighScoreDdbItem> loadAllHighScoresForUser(final String userId) {
        EverlastHighScoreDdbItem key = new EverlastHighScoreDdbItem();
        key.setUserId(userId);

        DynamoDBQueryExpression<EverlastHighScoreDdbItem> query = new DynamoDBQueryExpression<EverlastHighScoreDdbItem>()
                .withHashKeyValues(key)
                .withScanIndexForward(true);

        PaginatedQueryList<EverlastHighScoreDdbItem> items = super.queryAllItems(query);

        // Copy to new array forces that all results are paginated through within this method.
        return new ArrayList<>(items);
    }

    /**
     * Get the top high scores for a specific level.
     */
    public QueryResultPage<EverlastHighScoreDdbItem> loadHighScoresForLevel(
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

        return super.querySinglePage(query);
    }
}
