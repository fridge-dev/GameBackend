package com.frjgames.dal.ddb.accessors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.frjgames.dal.ddb.items.EverlastHighScoreDdbItem;
import com.frjgames.dal.ddb.testutils.TestUtilDynamoDbAccessorTestBase;
import com.frjgames.dal.ddb.typeconverters.types.WorldLevelDdbType;
import java.util.Iterator;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 * Tests the {@link EverlastHighScoreDdbAccessor}
 *
 * @author fridge
 */
public class EverlastHighScoreDdbAccessorTest extends TestUtilDynamoDbAccessorTestBase<EverlastHighScoreDdbItem, EverlastHighScoreDdbAccessor> {

    private static final String USER_ID = "user-guy";
    private static final String WORLD_ID = "world-1";
    private static final String LEVEL_ID = "level-1";
    private static final String GHOST_DATA = "087gOZ&DFGOGpi7gfwpih4f_li4FI!#H$FUHZSfilau4gfo";
    private static final long TIMESTAMP = 12342345L;

    public EverlastHighScoreDdbAccessorTest() {
        super(EverlastHighScoreDdbItem.class, EverlastHighScoreDdbAccessor::new);
    }

    @Test
    public void createLoadUpdateLoad() throws Exception {
        EverlastHighScoreDdbItem item = newItem();

        // 1. Create
        accessor.createOrUpdateItem(item);

        // 2. Load
        Optional<EverlastHighScoreDdbItem> loadedItem = accessor.loadItem(item.getUserId(), item.getWorldAndLevelId());
        assertEquals(item, loadedItem.orElse(null));

        // 3. Update (is allowed)
        item.setGhostData(GHOST_DATA + "new-ghost-but-same-key");
        item.setTimestamp(System.currentTimeMillis());
        accessor.createOrUpdateItem(item);

        // 4. Load (again)
        loadedItem = accessor.loadItem(item.getUserId(), item.getWorldAndLevelId());
        assertEquals(item, loadedItem.orElse(null));
    }

    @Test
    public void loadAllHighScoresForUser() throws Exception {
        // Save bulk data
        iterateBulkData(1, 20, 30, (userNumber, worldNumber, levelNumber) -> {
            EverlastHighScoreDdbItem item = newItemForBulkIterator(userNumber, worldNumber, levelNumber);
            accessor.createOrUpdateItem(item);
        });

        // Load items
        Iterator<EverlastHighScoreDdbItem> itemIterator = accessor.loadAllHighScoresForUser("user-0").iterator();

        iterateBulkData(1, 20, 30, (userNumber, worldNumber, levelNumber) -> {
            // Iterator should always have a next item, so we don't check.
            EverlastHighScoreDdbItem item = itemIterator.next();

            EverlastHighScoreDdbItem expectedItem = newItemForBulkIterator(userNumber, worldNumber, levelNumber);
            assertEquals(expectedItem, item);
        });

        // Iterator should have no more items left.
        assertFalse(itemIterator.hasNext());
    }

    @Test
    public void loadHighScoresForLevel() throws Exception {
        // Save bulk data
        iterateBulkData(20, 10, 20, (userNumber, worldNumber, levelNumber) -> {
            EverlastHighScoreDdbItem item = newItemForBulkIterator(userNumber, worldNumber, levelNumber);
            accessor.createOrUpdateItem(item);
        });

        // Load items
        WorldLevelDdbType level = WorldLevelDdbType.builder()
                .worldId("world-0002")
                .levelId("level-0003")
                .build();
        QueryResultPage<EverlastHighScoreDdbItem> resultPage = accessor.loadHighScoresForLevel(level, null);

        // Assert scores are ordered in descending order.
        int highestScore = 10000000;
        for (EverlastHighScoreDdbItem item : resultPage.getResults()) {
            assertTrue(item.getScore() <= highestScore);
            highestScore = item.getScore();
        }
    }

    private void iterateBulkData(final int numUsers, final int numWorlds, final int numLevels, final IntegerTriConsumer triConsumer) {
        for (int userNumber = 0; userNumber < numUsers; userNumber++) {
            for (int worldNumber = 0; worldNumber < numWorlds; worldNumber++) {
                for (int levelNumber = 0; levelNumber < numLevels; levelNumber++) {
                    triConsumer.accept(userNumber, worldNumber, levelNumber);
                }
            }
        }
    }

    @FunctionalInterface
    private interface IntegerTriConsumer {
        void accept(int userNumber, int worldNumber, int levelNumber);
    }

    private EverlastHighScoreDdbItem newItemForBulkIterator(final int userNumber, final int worldNumber, final int levelNumber) {
        // Set deterministic, "random" ish fields to make assertEquals() check less likely of collision.
        String userId = "user-" + userNumber;
        WorldLevelDdbType worldLevelId = WorldLevelDdbType.builder()
                .worldId("world-" + pad(worldNumber))
                .levelId("level-" + pad(levelNumber))
                .build();
        long timestamp = userNumber * 100000 + worldNumber * 1000 + levelNumber * 10;
        String ghostData = StringUtils.repeat("W" + worldNumber + "::L" + levelNumber + "::", 500);
        int score = 100 + String.format("u%s_w%s_l%s", userNumber, worldNumber, levelNumber).hashCode() % 30;

        EverlastHighScoreDdbItem item = new EverlastHighScoreDdbItem();
        item.setUserId(userId);
        item.setWorldAndLevelId(worldLevelId);
        item.setTimestamp(timestamp);
        item.setGhostData(ghostData);
        item.setScore(score);

        return item;
    }

    private String pad(final int num) {
        String str = "" + num;
        int numPadChars = 4 - str.length();

        return StringUtils.repeat("0", numPadChars) + str;
    }

    private EverlastHighScoreDdbItem newItem() {
        WorldLevelDdbType worldLevelDdbType = WorldLevelDdbType.builder()
                .worldId(WORLD_ID)
                .levelId(LEVEL_ID)
                .build();

        EverlastHighScoreDdbItem item = new EverlastHighScoreDdbItem();
        item.setUserId(USER_ID);
        item.setWorldAndLevelId(worldLevelDdbType);
        item.setGhostData(GHOST_DATA);
        item.setScore(10);
        item.setTimestamp(TIMESTAMP);

        return item;
    }
}