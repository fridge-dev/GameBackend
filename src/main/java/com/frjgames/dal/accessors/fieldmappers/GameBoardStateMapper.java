package com.frjgames.dal.accessors.fieldmappers;

import com.frjgames.dal.ddb.typeconverters.types.GameBoardPlayerDdbType;
import com.frjgames.dal.models.enums.CellOwnerEnum;
import com.frjgames.dal.models.enums.PlayerEnum;
import com.frjgames.dal.models.exceptions.InvalidDataException;
import com.frjgames.utils.FrjConditions;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.experimental.UtilityClass;

/**
 * DDB <-> App mapper between the types
 *
 * - DDB: {@link com.frjgames.dal.ddb.items.GameBoardDdbItem#gameBoardState}
 * - App: {@link com.frjgames.dal.models.data.GameBoard#gameBoardState}
 *
 * @author fridge
 */
@UtilityClass
public class GameBoardStateMapper {

    /**
     * App -> DDB type
     *
     * @see #unconvertBoardForAppLayer(List)
     */
    public static List<List<GameBoardPlayerDdbType>> convertBoardForPersistence(final List<List<CellOwnerEnum>> appBoard) {
        List<List<GameBoardPlayerDdbType>> ddbBoard = new ArrayList<>();

        for (List<CellOwnerEnum> innerAppBoard : appBoard) {
            List<GameBoardPlayerDdbType> innerDdbBoard = new ArrayList<>();

            for (CellOwnerEnum player : innerAppBoard) {
                innerDdbBoard.add(convertPlayerForBoard(player));
            }

            ddbBoard.add(innerDdbBoard);
        }

        return ddbBoard;
    }

    private static GameBoardPlayerDdbType convertPlayerForBoard(final CellOwnerEnum appType) {
        Preconditions.checkArgument(null != appType);

        switch (appType) {
            case PLAYER_ONE:
                return GameBoardPlayerDdbType.PLAYER_ONE;
            case PLAYER_TWO:
                return GameBoardPlayerDdbType.PLAYER_TWO;
            case OPEN:
                return GameBoardPlayerDdbType.NEITHER;
            default:
                throw new IllegalArgumentException("Invalid cell owner: " + appType);
        }
    }

    /**
     * DDB -> App type
     *
     * @see #convertBoardForPersistence(List)
     */
    public static List<List<CellOwnerEnum>> unconvertBoardForAppLayer(final List<List<GameBoardPlayerDdbType>> ddbBoard) {
        List<List<CellOwnerEnum>> appBoard = new ArrayList<>(ddbBoard.size());

        for (List<GameBoardPlayerDdbType> innerDdbBoard : ddbBoard) {
            // To be converted to a fixed-size ArrayList
            CellOwnerEnum[] innerAppBoardArr = new CellOwnerEnum[innerDdbBoard.size()];

            for (int j = 0; j < innerDdbBoard.size(); j++) {
                GameBoardPlayerDdbType player = innerDdbBoard.get(j);
                innerAppBoardArr[j] = unconvertPlayerForBoard(player);
            }

            appBoard.add(Arrays.asList(innerAppBoardArr));
        }

        return appBoard;
    }

    private static CellOwnerEnum unconvertPlayerForBoard(final GameBoardPlayerDdbType ddbType) {
        FrjConditions.checkNotNull(ddbType, () -> new InvalidDataException("Database GameState's next move cannot be null."));

        switch (ddbType) {
            case PLAYER_ONE:
                return CellOwnerEnum.PLAYER_ONE;
            case PLAYER_TWO:
                return CellOwnerEnum.PLAYER_TWO;
            case NEITHER:
                return CellOwnerEnum.OPEN;
            default:
                throw new IllegalArgumentException("Invalid cell owner: " + ddbType);
        }
    }

    /**
     * App -> DDB type
     *
     * @see #unconvertPlayer(GameBoardPlayerDdbType)
     */
    public static GameBoardPlayerDdbType convertPlayer(final PlayerEnum appType) {
        Preconditions.checkArgument(null != appType);

        switch (appType) {
            case ONE:
                return GameBoardPlayerDdbType.PLAYER_ONE;
            case TWO:
                return GameBoardPlayerDdbType.PLAYER_TWO;
            default:
                throw new IllegalArgumentException("Invalid next move: " + appType);
        }
    }

    /**
     * DDB -> App type
     *
     * @see #convertPlayer(PlayerEnum)
     */
    public static PlayerEnum unconvertPlayer(final GameBoardPlayerDdbType ddbType) {
        FrjConditions.checkNotNull(ddbType, () -> new InvalidDataException("Database GameState's next move cannot be null."));

        switch (ddbType) {
            case PLAYER_ONE:
                return PlayerEnum.ONE;
            case PLAYER_TWO:
                return PlayerEnum.TWO;
            case NEITHER:
                throw new InvalidDataException("Database GameState's next move cannot be nobody.");
            default:
                throw new IllegalArgumentException("Unrecognized Player DDB type: " + ddbType);
        }
    }
}
