package com.frjgames.dal.accessors.fieldmappers;

import static org.junit.Assert.assertEquals;

import com.frjgames.dal.ddb.typeconverters.types.GameBoardPlayerDdbType;
import com.frjgames.dal.models.enums.CellOwnerEnum;
import com.frjgames.dal.models.enums.PlayerEnum;
import com.frjgames.dal.models.exceptions.InvalidDataException;
import com.frjgames.testutils.TestUtilExceptionValidator;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

/**
 * Tests the {@link GameBoardStateMapper} class.
 *
 * @author fridge
 */
public class GameBoardStateMapperTest {

    /**
     * Two equivalent boards.
     */
    private static final List<List<CellOwnerEnum>> APP_BOARD = Arrays.asList(
            Arrays.asList(CellOwnerEnum.PLAYER_ONE, CellOwnerEnum.PLAYER_ONE, CellOwnerEnum.OPEN),
            Arrays.asList(CellOwnerEnum.PLAYER_TWO, CellOwnerEnum.PLAYER_TWO, CellOwnerEnum.OPEN),
            Arrays.asList(CellOwnerEnum.PLAYER_ONE, CellOwnerEnum.PLAYER_TWO, CellOwnerEnum.PLAYER_ONE)
    );
    private static final List<List<GameBoardPlayerDdbType>> DDB_BOARD = Arrays.asList(
            Arrays.asList(GameBoardPlayerDdbType.PLAYER_ONE, GameBoardPlayerDdbType.PLAYER_ONE, GameBoardPlayerDdbType.NEITHER),
            Arrays.asList(GameBoardPlayerDdbType.PLAYER_TWO, GameBoardPlayerDdbType.PLAYER_TWO, GameBoardPlayerDdbType.NEITHER),
            Arrays.asList(GameBoardPlayerDdbType.PLAYER_ONE, GameBoardPlayerDdbType.PLAYER_TWO, GameBoardPlayerDdbType.PLAYER_ONE)
    );

    @Test
    public void convertBoardForPersistence_ThenUnconvert() throws Exception {
        List<List<GameBoardPlayerDdbType>> ddbBoard = GameBoardStateMapper.convertBoardForPersistence(APP_BOARD);

        assertEquals(DDB_BOARD, ddbBoard);

        assertEquals(APP_BOARD, GameBoardStateMapper.unconvertBoardForAppLayer(ddbBoard));
    }

    @Test
    public void unconvertBoardForAppLayer_ThenConvert() throws Exception {
        List<List<CellOwnerEnum>> appBoard = GameBoardStateMapper.unconvertBoardForAppLayer(DDB_BOARD);

        assertEquals(APP_BOARD, appBoard);

        assertEquals(DDB_BOARD, GameBoardStateMapper.convertBoardForPersistence(appBoard));
    }

    @Test
    public void convertPlayer() throws Exception {
        assertEquals(GameBoardPlayerDdbType.PLAYER_ONE, GameBoardStateMapper.convertPlayer(PlayerEnum.ONE));
        assertEquals(GameBoardPlayerDdbType.PLAYER_TWO, GameBoardStateMapper.convertPlayer(PlayerEnum.TWO));
        TestUtilExceptionValidator.validateIllegalArg(() -> GameBoardStateMapper.convertPlayer(null));
    }

    @Test
    public void unconvertPlayer() throws Exception {
        assertEquals(PlayerEnum.ONE, GameBoardStateMapper.unconvertPlayer(GameBoardPlayerDdbType.PLAYER_ONE));
        assertEquals(PlayerEnum.TWO, GameBoardStateMapper.unconvertPlayer(GameBoardPlayerDdbType.PLAYER_TWO));
        TestUtilExceptionValidator.validateThrown(InvalidDataException.class, () -> GameBoardStateMapper.unconvertPlayer(GameBoardPlayerDdbType.NEITHER));
        TestUtilExceptionValidator.validateThrown(InvalidDataException.class, () -> GameBoardStateMapper.unconvertPlayer(null));
    }

    @Test
    public void convertUnconvertPlayer() throws Exception {
        assertEquals(PlayerEnum.ONE, GameBoardStateMapper.unconvertPlayer(GameBoardStateMapper.convertPlayer(PlayerEnum.ONE)));
        assertEquals(PlayerEnum.TWO, GameBoardStateMapper.unconvertPlayer(GameBoardStateMapper.convertPlayer(PlayerEnum.TWO)));
    }
}