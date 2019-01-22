package com.frjgames.dal.accessors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.frjgames.dal.ddb.items.EverlastHighScoreDdbItem;
import com.frjgames.dal.ddb.testutils.TestUtilDynamoDbLocalTestBase;
import com.frjgames.dal.models.data.EverlastHighScore;
import com.frjgames.dal.models.data.PaginatedResult;
import com.frjgames.dal.models.interfaces.EverlastHighScoreAccessor;
import com.frjgames.dal.models.keys.EverlastHighScoreDataKey;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Functional integ test for the {@link EverlastHighScoreAccessorImplV2} class.
 *
 * @author fridge
 */
public class EverlastHighScoreAccessorImplV2Test extends TestUtilDynamoDbLocalTestBase<EverlastHighScoreDdbItem> {

    private static final String USER_ID = "user-guy";
    private static final String WORLD_ID = "world-1";
    private static final String LEVEL_ID = "level-1";
    private static final String GHOST_DATA = "087gOZ&DFGOGpi7gfwpih4f_li4FI!#H$FUHZSfilau4gfo";
    private static final long TIMESTAMP = 12342345L;

    private EverlastHighScoreAccessor accessor;

    public EverlastHighScoreAccessorImplV2Test() {
        super(EverlastHighScoreDdbItem.class);
    }

    @Before
    public void setup() {
        accessor = getDalModule().everlastHighScoreAccessor();
    }

    @Test
    public void createLoadUpdateLoad() throws Exception {
        EverlastHighScore data = newData();
        EverlastHighScoreDataKey key = EverlastHighScoreDataKey.builder()
                .userId(data.getUserId())
                .worldId(data.getWorldId())
                .levelId(data.getLevelId())
                .build();

        // 1. Create
        accessor.create(data);

        // 2. Load
        EverlastHighScore loadedData = accessor.load(key).orElse(null);
        assertEquals(data, loadedData);

        // 3. Update (is allowed)
        data = data.toBuilder()
                .ghostData(GHOST_DATA + "new-ghost-but-same-key")
                .timestamp(System.currentTimeMillis())
                .build();
        accessor.create(data);

        // 4. Load (again)
        EverlastHighScore loadedData2 = accessor.load(key).orElse(null);
        assertEquals(data, loadedData2);
        assertNotEquals(loadedData, loadedData2);
    }


    @Test
    public void loadAllHighScoresForUser() throws Exception {
        // Save bulk data
        iterateBulkData(1, 20, 30, (userNumber, worldNumber, levelNumber) -> {
            EverlastHighScore data = newDataForBulkIterator(userNumber, worldNumber, levelNumber);
            accessor.create(data);
        });

        // Load items
        Iterator<EverlastHighScore> dataIterator = accessor.loadAllForUser("user-0").iterator();

        iterateBulkData(1, 20, 30, (userNumber, worldNumber, levelNumber) -> {
            // Iterator should always have a next item, so we don't check.
            EverlastHighScore data = dataIterator.next();

            EverlastHighScore expectedData = newDataForBulkIterator(userNumber, worldNumber, levelNumber);
            assertEquals(expectedData, data);
        });

        // Iterator should have no more items left.
        assertFalse(dataIterator.hasNext());
    }

    @Test
    public void loadHighScoresForLevel() throws Exception {
        // 1. Save bulk data
        iterateBulkData(200, 1, 1, (userNumber, worldNumber, levelNumber) -> {
            EverlastHighScore data = newDataForBulkIterator(userNumber, worldNumber, levelNumber);
            accessor.create(data);
        });

        List<EverlastHighScore> allResults = new ArrayList<>();

        // 2. Load items (first page)
        String worldId = "world-0000";
        String levelId = "level-0000";
        PaginatedResult<EverlastHighScore> page = accessor.loadForLevel(worldId, levelId);
        allResults.addAll(page.getResults());

        // 3. Load rest of pages
        String paginationToken = page.getPaginationToken().orElse(null);
        assertNotNull("Test should be re-written to ensure there is enough test data to paginate", paginationToken);
        while (paginationToken != null) {
            page = accessor.loadForLevel(worldId, levelId, paginationToken);

            allResults.addAll(page.getResults());

            paginationToken = page.getPaginationToken().orElse(null);
        }
        assertEquals(200, allResults.size());

        // 4. Assert scores are ordered in descending order.
        int highestScore = 10000000;
        for (EverlastHighScore data : allResults) {
            assertTrue(data.getScore() <= highestScore);
            highestScore = data.getScore();
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

    private EverlastHighScore newDataForBulkIterator(final int userNumber, final int worldNumber, final int levelNumber) {
        // Set deterministic, "random" ish fields to make assertEquals() check less likely of collision.
        String userId = "user-" + userNumber;
        String worldId = "world-" + pad(worldNumber);
        String levelId = "level-" + pad(levelNumber);
        String ghostData = StringUtils.repeat("W" + worldNumber + "::L" + levelNumber + "::", 5000);
        int score = 100 + String.format("u%s_w%s_l%s", userNumber, worldNumber, levelNumber).hashCode() % 30;
        long timestamp = userNumber * 100000 + worldNumber * 1000 + levelNumber * 10;

        return EverlastHighScore.builder()
                .userId(userId)
                .worldId(worldId)
                .levelId(levelId)
                .ghostData(ghostData)
                .score(score)
                .timestamp(timestamp)
                .build();
    }

    private String pad(final int num) {
        String str = "" + num;
        int numPadChars = 4 - str.length();

        return StringUtils.repeat("0", numPadChars) + str;
    }

    private EverlastHighScore newData() {
        return EverlastHighScore.builder()
                .userId(USER_ID)
                .worldId(WORLD_ID)
                .levelId(LEVEL_ID)
                .ghostData(GHOST_DATA)
                .score(10)
                .timestamp(TIMESTAMP)
                .build();
    }
}