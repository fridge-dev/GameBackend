package com.frjgames.app.api.handlers;

import static org.junit.Assert.assertTrue;

import com.frjgames.app.api.handlers.integ.testutils.ApiHandleIntegTestBase;
import com.frjgames.app.api.models.exceptions.DuplicateGameException;
import com.frjgames.app.api.models.exceptions.IllegalGameUpdateException;
import com.frjgames.app.api.models.exceptions.InvalidInputException;
import com.frjgames.app.api.models.inputs.CreateGameInput;
import com.frjgames.app.api.models.inputs.MatchGameInput;
import com.frjgames.app.api.models.interfaces.CreateGameHandler;
import com.frjgames.app.api.models.interfaces.MatchGameHandler;
import com.frjgames.dal.ddb.items.GameDdbItem;
import com.frjgames.dal.ddb.items.UserSessionDdbItem;
import com.frjgames.testutils.TestUtilExceptionValidator;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;

/**
 * Functional integ tests for the {@link GameHandlerImpl} class.
 *
 * @author fridge
 */
public class GameHandlerImplIntegTest extends ApiHandleIntegTestBase {

    private static final String USER_1 = "Frj-guid1";
    private static final String USER_2 = "BadGuy2";

    private GameHandlerImpl gameHandler;
    private String gameId;

    public GameHandlerImplIntegTest() {
        super(GameDdbItem.class, UserSessionDdbItem.class);
    }

    @Before
    public void setup() throws Exception {
        assertGameHandlerImplInterfaces();
        gameId = UUID.randomUUID().toString();
    }

    private void assertGameHandlerImplInterfaces() {
        CreateGameHandler createGameHandler = super.getApiHandlerModule().getCreateGameHandler();
        MatchGameHandler matchGameHandler = super.getApiHandlerModule().getMatchGameHandler();

        assertTrue(createGameHandler instanceof GameHandlerImpl);
        assertTrue(matchGameHandler instanceof GameHandlerImpl);
        assertTrue(createGameHandler == matchGameHandler);

        gameHandler = (GameHandlerImpl) createGameHandler;
    }

    private CreateGameInput newCreateGameInput(final String userId) {
        return CreateGameInput.builder()
                .gameId(gameId)
                .gameName("No noobs plz")
                .ownerUserId(userId)
                .creationTimeMs(System.currentTimeMillis())
                .build();
    }

    private MatchGameInput newMatchGameInput(final String userId) {
        return MatchGameInput.builder()
                .gameId(gameId)
                .playerId(userId)
                .build();
    }

    @Test
    public void createGame_MatchGame() throws Exception {
        // 1. Create new game
        CreateGameInput createGameInput = newCreateGameInput(USER_1);
        gameHandler.createGame(createGameInput);

        // 2. Match the game
        MatchGameInput matchGameInput = newMatchGameInput(USER_2);
        gameHandler.matchGame(matchGameInput);
    }

    @Test
    public void createGame_DuplicateCreate() throws Exception {
        // 1. Create new game
        CreateGameInput createGameInput = newCreateGameInput(USER_1);
        gameHandler.createGame(createGameInput);

        // 2. dupe
        TestUtilExceptionValidator.assertThrows(DuplicateGameException.class, () -> gameHandler.createGame(createGameInput));
    }

    @Test
    public void matchGameDne() throws Exception {
        MatchGameInput matchGameInput = newMatchGameInput(USER_2);
        TestUtilExceptionValidator.assertThrows(InvalidInputException.class, () -> gameHandler.matchGame(matchGameInput));
    }

    @Test
    public void createGame_MatchGame_DuplicateMatch() throws Exception {
        // 1. Create new game
        CreateGameInput createGameInput = newCreateGameInput(USER_1);
        gameHandler.createGame(createGameInput);

        // 2. Match the game
        MatchGameInput matchGameInput = newMatchGameInput(USER_2);
        gameHandler.matchGame(matchGameInput);

        // 3. dupe
        TestUtilExceptionValidator.assertThrows(IllegalGameUpdateException.class, () -> gameHandler.matchGame(matchGameInput));
    }

    @Test
    public void createGame_MatchGame_MatchWithOwner() throws Exception {
        // 1. Create new game
        CreateGameInput createGameInput = newCreateGameInput(USER_1);
        gameHandler.createGame(createGameInput);

        // 2. Match the game with the owner
        MatchGameInput matchGameInput = newMatchGameInput(USER_1);
        TestUtilExceptionValidator.assertThrows(IllegalGameUpdateException.class, () -> gameHandler.matchGame(matchGameInput));
    }
}