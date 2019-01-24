package com.frjgames.dal.ddb.items;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBVersionAttribute;
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
    private List<List<Integer>> gameBoardState;

    /**
     * 0 => game over
     * 1 => player 1
     * 2 => player 2
     */
    @DynamoDBAttribute(attributeName = COL_NEXT_MOVE)
    private int nextMove;

    /**
     * Used for optimistic locking.
     */
    @DynamoDBVersionAttribute(attributeName = COL_OPTIMISTIC_LOCKING_VERSION)
    private Long optimisticLockingVersion;

}
