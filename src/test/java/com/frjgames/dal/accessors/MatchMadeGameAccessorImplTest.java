package com.frjgames.dal.accessors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.frjgames.dal.ddb.items.GameDdbItem;
import com.frjgames.dal.ddb.testutils.TestUtilDynamoDbLocalTestBase;
import com.frjgames.dal.models.data.MatchMadeGame;
import com.frjgames.dal.models.exceptions.ConditionalWriteException;
import com.frjgames.dal.models.interfaces.MatchMadeGameAccessor;
import com.frjgames.dal.models.keys.GameIdKey;
import com.frjgames.testutils.TestUtilExceptionValidator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Functional integ test for the {@link MatchMadeGameAccessorImpl} class.
 *
 * @author fridge
 */
public class MatchMadeGameAccessorImplTest extends TestUtilDynamoDbLocalTestBase<GameDdbItem> {

    private static final String GAME_ID = "lsuhg";
    private static final String GAME_NAME = "why-not-zoidberg";
    private static final String HOST_USER_ID = "host-user";
    private static final long TIME_MS = System.currentTimeMillis();

    private MatchMadeGameAccessor gameAccessor;

    public MatchMadeGameAccessorImplTest() {
        super(GameDdbItem.class);
    }

    @Before
    public void setup() {
        gameAccessor = getDalModule().matchMadeGameAccessor();
    }

    @Test
    public void create_Load() throws Exception {
        MatchMadeGame persistedGame = newData();
        GameIdKey key = GameIdKey.builder()
                .gameId(newData().getGameId())
                .build();

        // 1. Load (empty)
        assertFalse(gameAccessor.load(key).isPresent());
        assertFalse(gameAccessor.loadByName(GAME_NAME).isPresent());

        // 2. Create
        gameAccessor.create(persistedGame);

        // 3. Load
        MatchMadeGame expectedGame = persistedGame.toBuilder()
                .isGameUnmatched(true)
                .build();
        assertEquals(expectedGame, gameAccessor.load(key).orElse(null));
        assertEquals(expectedGame, gameAccessor.loadByName(GAME_NAME).orElse(null));

        // 4. Create (duplicate ID)
        TestUtilExceptionValidator.validateThrown(
                ConditionalWriteException.class,
                () -> gameAccessor.create(persistedGame)
        );

        // 5. Create (duplicate name)
        MatchMadeGame duplicateNameGame = persistedGame.toBuilder()
                .gameId("other-id")
                .build();
        gameAccessor.create(duplicateNameGame);

        // 6. Load by name, simple assertion
        assertTrue(gameAccessor.loadByName(GAME_NAME).isPresent());
    }

    @Test
    public void loadAvailableGames() throws Exception {
        // 01/24/2019 @ 12:00pm (UTC)
        long baseTime = 1548331200000L;

        // Create items
        int numGames = 10;
        for (int i = 0; i < numGames; i++) {
            long timeToDeduct = TimeUnit.MINUTES.toMillis(10L) + i * 1000;
            gameAccessor.create(newData(i, baseTime - timeToDeduct));
        }

        // Query for "baseTime", which will fall back to previous time window
        List<MatchMadeGame> firstResult = gameAccessor.loadAvailableGames(baseTime);
        assertEquals(numGames, firstResult.size());

        // Query for the previous time window and ensure we get the same result
        long previousTime = baseTime - 1; // 11:59am
        List<MatchMadeGame> secondResult = gameAccessor.loadAvailableGames(previousTime);
        assertEquals(firstResult, secondResult);
    }

    private MatchMadeGame newData() {
        return MatchMadeGame.builder()
                .gameId(GAME_ID)
                .gameName(GAME_NAME)
                .hostUserId(HOST_USER_ID)
                .creationTimeMs(TIME_MS)
                .build();
    }

    private MatchMadeGame newData(final int gameId, final long timeMs) {
        return MatchMadeGame.builder()
                .gameId(GAME_ID + gameId)
                .gameName(RandomStringUtils.random(4))
                .hostUserId(RandomStringUtils.random(4))
                .creationTimeMs(timeMs)
                .build();
    }
}