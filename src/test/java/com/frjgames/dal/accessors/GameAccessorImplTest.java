package com.frjgames.dal.accessors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import com.frjgames.dal.ddb.items.GameDdbItem;
import com.frjgames.dal.ddb.testutils.TestUtilDynamoDbLocalTestBase;
import com.frjgames.dal.models.data.Game;
import com.frjgames.dal.models.data.MatchMadeGame;
import com.frjgames.dal.models.enums.GameStatusResultType;
import com.frjgames.dal.models.exceptions.InvalidDataException;
import com.frjgames.dal.models.interfaces.GameAccessor;
import com.frjgames.dal.models.interfaces.MatchMadeGameAccessor;
import com.frjgames.dal.models.keys.GameIdKey;
import com.frjgames.testutils.TestUtilExceptionValidator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Functional integ test for the {@link GameAccessorImpl} class, also depends on {@link MatchMadeGameAccessorImpl} class.
 *
 * @author fridge
 */
public class GameAccessorImplTest extends TestUtilDynamoDbLocalTestBase<GameDdbItem> {

    private static final String GAME_ID = "lsuhg";
    private static final String GAME_NAME = "why-not-zoidberg";
    private static final String HOST_USER_ID = "host-user";
    private static final String GUEST_USER_ID = "guest-user";
    private static final long TIME_MS = System.currentTimeMillis();

    private GameAccessor gameAccessor;
    private MatchMadeGameAccessor matchMadeGameAccessor;
    private GameIdKey key;

    public GameAccessorImplTest() {
        super(GameDdbItem.class);
    }

    @Before
    public void setup() {
        gameAccessor = super.getDalModule().gameAccessor();
        matchMadeGameAccessor = super.getDalModule().matchMadeGameAccessor();
        key = GameIdKey.builder()
                .gameId(GAME_ID)
                .build();
    }

    private void commonSetupAndAssertions() {
        // 1. Load (empty)
        assertFalse(gameAccessor.load(key).isPresent());

        // 2. Create (via match-making)
        matchMadeGameAccessor.create(newMatchMadeGame());

        // 3. Load and validate
        Game game = gameAccessor.load(key).orElseGet(this::fail);
        assertEquals(GAME_ID, game.getGameId());
        assertEquals(HOST_USER_ID, game.getHostUserId());
        assertNull(game.getGuestUserId());
        assertEquals(GameStatusResultType.UNMATCHED, game.getStatus());
        assertFalse(game.getWinnerUserId().isPresent());

        // 4. Match (via match-making)
        matchMadeGameAccessor.updateMatchWithGuestUser(key, GUEST_USER_ID);

        // 5. Load and validate
        game = gameAccessor.load(key).orElseGet(this::fail);
        assertEquals(GAME_ID, game.getGameId());
        assertEquals(HOST_USER_ID, game.getHostUserId());
        assertEquals(GUEST_USER_ID, game.getGuestUserId());
        assertEquals(GameStatusResultType.IN_PROGRESS, game.getStatus());
        assertFalse(game.getWinnerUserId().isPresent());
    }

    private MatchMadeGame newMatchMadeGame() {
        return MatchMadeGame.builder()
                .gameId(GAME_ID)
                .gameName(GAME_NAME)
                .hostUserId(HOST_USER_ID)
                .creationTimeMs(TIME_MS)
                .build();
    }

    private <T> T fail() {
        Assert.fail("Expected Optional to be present");
        return null; // line above will throw exception, but this line is needed for compiler
    }

    private void assertUpdateGameCompleted(final GameStatusResultType statusType, final String winnerUserId) {
        gameAccessor.updateGameCompleted(key, statusType, winnerUserId);

        Game game = gameAccessor.load(key).orElseGet(this::fail);
        assertEquals(GAME_ID, game.getGameId());
        assertEquals(HOST_USER_ID, game.getHostUserId());
        assertEquals(GUEST_USER_ID, game.getGuestUserId());
        assertEquals(statusType, game.getStatus());
        assertEquals(winnerUserId, game.getWinnerUserId().orElse(null));
    }

    @Test
    public void updateGameCompleted_WinnerHost() throws Exception {
        commonSetupAndAssertions();

        assertUpdateGameCompleted(GameStatusResultType.COMPLETED_VICTORY, HOST_USER_ID);
    }

    @Test
    public void updateGameCompleted_WinnerGuest() throws Exception {
        commonSetupAndAssertions();

        assertUpdateGameCompleted(GameStatusResultType.COMPLETED_VICTORY, GUEST_USER_ID);
    }

    @Test
    public void updateGameCompleted_ForfeitHost() throws Exception {
        commonSetupAndAssertions();

        assertUpdateGameCompleted(GameStatusResultType.COMPLETED_FORFEIT, HOST_USER_ID);
    }

    @Test
    public void updateGameCompleted_ForfeitGuest() throws Exception {
        commonSetupAndAssertions();

        assertUpdateGameCompleted(GameStatusResultType.COMPLETED_FORFEIT, GUEST_USER_ID);
    }

    @Test
    public void updateGameCompleted_Draw() throws Exception {
        commonSetupAndAssertions();

        assertUpdateGameCompleted(GameStatusResultType.COMPLETED_DRAW, null);
    }

    @Test
    public void updateGameCompleted_Duplicate() throws Exception {
        commonSetupAndAssertions();

        gameAccessor.updateGameCompleted(key, GameStatusResultType.COMPLETED_VICTORY, HOST_USER_ID);

        // Duplicate call fails
        TestUtilExceptionValidator.validateThrown(
                InvalidDataException.class,
                () -> gameAccessor.updateGameCompleted(key, GameStatusResultType.COMPLETED_VICTORY, HOST_USER_ID)
        );
    }

    @Test
    public void updateGameCompleted_FromStatusUnmatched() throws Exception {
        matchMadeGameAccessor.create(newMatchMadeGame());

        TestUtilExceptionValidator.validateThrown(
                InvalidDataException.class,
                () -> gameAccessor.updateGameCompleted(key, GameStatusResultType.COMPLETED_VICTORY, HOST_USER_ID)
        );
    }

    @Test
    public void updateGameCompleted_ToInvalidStatus() throws Exception {
        commonSetupAndAssertions();

        TestUtilExceptionValidator.validateIllegalArg(() -> gameAccessor.updateGameCompleted(key, GameStatusResultType.DELETED, "not-you"));
    }

    @Test
    public void updateGameCompleted_WinnerNotPlayer() throws Exception {
        commonSetupAndAssertions();

        TestUtilExceptionValidator.validateIllegalArg(() -> gameAccessor.updateGameCompleted(key, GameStatusResultType.COMPLETED_VICTORY, "not-you"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void create() throws Exception {
        gameAccessor.create(null);
    }
}