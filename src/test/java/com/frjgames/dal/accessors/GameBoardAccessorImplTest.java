package com.frjgames.dal.accessors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import com.frjgames.dal.ddb.items.GameBoardDdbItem;
import com.frjgames.dal.ddb.testutils.TestUtilDynamoDbLocalTestBase;
import com.frjgames.dal.models.data.GameBoard;
import com.frjgames.dal.models.enums.CellOwnerEnum;
import com.frjgames.dal.models.enums.PlayerEnum;
import com.frjgames.dal.models.exceptions.ConditionalWriteException;
import com.frjgames.dal.models.interfaces.GameBoardAccessor;
import com.frjgames.dal.models.keys.GameIdKey;
import com.frjgames.testutils.TestUtilExceptionValidator;
import com.frjgames.testutils.argmatcher.TestUtilBoardUtils;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the {@link GameBoardAccessorImpl} class.
 *
 * @author fridge
 */
public class GameBoardAccessorImplTest extends TestUtilDynamoDbLocalTestBase<GameBoardDdbItem> {

    private GameBoardAccessor gameBoardAccessor;
    private String gameId;
    private GameIdKey key;

    public GameBoardAccessorImplTest() {
        super(GameBoardDdbItem.class);
    }

    @Before
    public void setup() {
        gameBoardAccessor = super.getDalModule().gameBoardAccessor();
        gameId = RandomStringUtils.random(7);
        key = GameIdKey.builder()
                .gameId(gameId)
                .build();
    }

    @Test
    public void create_Load() throws Exception {
        // 1. Load (empty)
        assertFalse(gameBoardAccessor.load(key).isPresent());

        // 2. Create
        List<List<CellOwnerEnum>> cells = TestUtilBoardUtils.generateRandomBoard(CellOwnerEnum.class, 9);
        gameBoardAccessor.create(GameBoard.builder()
                .gameId(gameId)
                .nextBoardIndex(1)
                .nextMove(PlayerEnum.ONE)
                .gameBoardState(cells)
                .build());

        // 3. Load and validate
        GameBoard gameBoard = gameBoardAccessor.load(key).orElse(null);
        assertNotNull(gameBoard);
        assertEquals(gameId, gameBoard.getGameId());
        assertEquals(1, gameBoard.getNextBoardIndex());
        assertEquals(PlayerEnum.ONE, gameBoard.getNextMove());
        assertEquals(cells, gameBoard.getGameBoardState());
        assertNotNull(gameBoard.getBoardVersion());

        // 4. Duplicate Create (rejected)
        TestUtilExceptionValidator.validateThrown(ConditionalWriteException.class, () -> gameBoardAccessor.create(gameBoard));
    }

    @Test
    public void updateBoardWithMove() throws Exception {
        // 1. Create
        List<List<CellOwnerEnum>> boardState = TestUtilBoardUtils.generateRandomBoard(CellOwnerEnum.class, 9);
        boardState.get(2).set(3, CellOwnerEnum.PLAYER_ONE);
        gameBoardAccessor.create(GameBoard.builder()
                .gameId(gameId)
                .nextBoardIndex(1)
                .nextMove(PlayerEnum.ONE)
                .gameBoardState(boardState)
                .build());

        // 2. Load
        GameBoard gameBoard = gameBoardAccessor.load(key).orElse(null);
        assertNotNull(gameBoard);

        // 3. Update
        gameBoard.getGameBoardState().get(2).set(3, CellOwnerEnum.PLAYER_TWO);
        gameBoardAccessor.updateBoardWithMove(gameBoard);

        // 4. Load
        GameBoard gameBoard2 = gameBoardAccessor.load(key).orElse(null);
        assertNotNull(gameBoard2);
        assertEquals(gameBoard.getGameBoardState(), gameBoard2.getGameBoardState());
        assertNotEquals(gameBoard.getBoardVersion(), gameBoard2.getBoardVersion());

        // 5. Duplicate update (rejected).
        TestUtilExceptionValidator.validateThrown(ConditionalWriteException.class, () -> gameBoardAccessor.updateBoardWithMove(gameBoard));
    }

}