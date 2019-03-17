package com.frjgames.dal.accessors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.frjgames.dal.ddb.items.GameDdbItem;
import com.frjgames.dal.ddb.testutils.TestUtilDynamoDbLocalTestBase;
import com.frjgames.dal.models.data.MatchMadeGame;
import com.frjgames.dal.models.exceptions.ConditionalWriteException;
import com.frjgames.dal.models.exceptions.InvalidDataException;
import com.frjgames.dal.models.exceptions.MissingDataException;
import com.frjgames.dal.models.interfaces.MatchMadeGameAccessor;
import com.frjgames.dal.models.keys.GameIdKey;
import com.frjgames.testutils.TestUtilExceptionValidator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
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
    private static final String GUEST_USER_ID = "guest-user";
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
                .gameId(persistedGame.getGameId())
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
        TestUtilExceptionValidator.assertThrows(
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

    @Test
    public void updateMatchWithGuestUser() throws Exception {
        MatchMadeGame persistedGame = newData();
        GameIdKey key = GameIdKey.builder()
                .gameId(persistedGame.getGameId())
                .build();

        // 1. Save game
        gameAccessor.create(persistedGame);

        // 2. Update game
        gameAccessor.updateMatchWithGuestUser(key, GUEST_USER_ID);

        // 3. Load and verify
        MatchMadeGame loadedGame = gameAccessor.load(key).orElseGet(this::fail);
        assertFalse(loadedGame.isGameUnmatched());
    }

    private <T> T fail() {
        Assert.fail("Expected Optional to be present");
        return null; // line above will throw exception, but this line is needed for compiler
    }

    @Test(expected = MissingDataException.class)
    public void updateMatchWithGuestUser_GameDoesntExist() throws Exception {
        GameIdKey key = GameIdKey.builder()
                .gameId(GAME_ID)
                .build();

        gameAccessor.updateMatchWithGuestUser(key, GUEST_USER_ID);
    }

    @Test
    public void updateMatchWithGuestUser_AlreadyPaired() throws Exception {
        MatchMadeGame game = newData();
        GameIdKey key = GameIdKey.builder()
                .gameId(game.getGameId())
                .build();

        gameAccessor.create(game);

        gameAccessor.updateMatchWithGuestUser(key, GUEST_USER_ID);

        TestUtilExceptionValidator.assertThrows(InvalidDataException.class,
                () -> gameAccessor.updateMatchWithGuestUser(key, "other" + GUEST_USER_ID)
        );
    }

    @Test
    public void updateMatchWithGuestUser_GuestIsHost() throws Exception {
        MatchMadeGame game = newData();
        GameIdKey key = GameIdKey.builder()
                .gameId(game.getGameId())
                .build();

        gameAccessor.create(game);

        TestUtilExceptionValidator.assertThrows(InvalidDataException.class,
                () -> gameAccessor.updateMatchWithGuestUser(key, game.getHostUserId())
        );
    }

    @Test(expected = UnsupportedOperationException.class)
    public void delete() throws Exception {
        gameAccessor.delete(null);
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