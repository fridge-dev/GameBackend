package com.frjgames.dal.ddb.items;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBVersionAttribute;
import com.frjgames.dal.ddb.typeconverters.converters.GameBoardPlayerDdbTypeConverter;
import com.frjgames.dal.ddb.typeconverters.converters.GameBoardStateDdbTypeConverter;
import com.frjgames.dal.ddb.typeconverters.types.GameBoardPlayerDdbType;
import java.util.List;
import lombok.Data;

/**
 * DynamoDB item representing the specific game board state of a game.
 *
 * @see GameDdbItem
 *
 * @author fridge
 */
@Data
@DynamoDBTable(tableName = GameBoardDdbItem.TABLE_NAME)
public class GameBoardDdbItem implements DdbItem {

    public static final String TABLE_NAME = "GameBoards";

    private static final String COL_GAME_ID = "GameID";
    private static final String COL_GAME_BOARD_STATE = "GameBoardState";
    private static final String COL_NEXT_MOVE = "NextMoveUserID";
    private static final String COL_NEXT_BOARD_INDEX = "NextBoardIndex";
    private static final String COL_OPTIMISTIC_LOCKING_VERSION = "OptimisticLockVersion";

    /**
     * Unique ID of the game.
     */
    @DynamoDBHashKey(attributeName = COL_GAME_ID)
    private String gameId;

    /**
     * The nested ultimate tic-tac-toe board.
     * 0 => unused
     * 1 => player 1
     * 2 => player 2
     */
    @DynamoDBAttribute(attributeName = COL_GAME_BOARD_STATE)
    @DynamoDBTypeConverted(converter = GameBoardStateDdbTypeConverter.class)
    private List<List<GameBoardPlayerDdbType>> gameBoardState;

    /**
     * 0 => game over
     * 1 => player 1
     * 2 => player 2
     */
    @DynamoDBAttribute(attributeName = COL_NEXT_MOVE)
    @DynamoDBTypeConverted(converter = GameBoardPlayerDdbTypeConverter.class)
    private GameBoardPlayerDdbType nextMove;

    /**
     * The array index [0, 9) of which board the next move should go in.
     */
    @DynamoDBAttribute(attributeName = COL_NEXT_BOARD_INDEX)
    private int nextBoardIndex;

    /**
     * Used for optimistic locking.
     */
    @DynamoDBVersionAttribute(attributeName = COL_OPTIMISTIC_LOCKING_VERSION)
    private Long optimisticLockingVersion;

}
