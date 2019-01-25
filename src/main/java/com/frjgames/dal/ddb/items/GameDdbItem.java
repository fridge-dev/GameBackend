package com.frjgames.dal.ddb.items;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBVersionAttribute;
import com.frjgames.dal.ddb.typeconverters.types.GameResultType;
import com.frjgames.dal.ddb.typeconverters.types.GameStatusType;
import lombok.Data;

/**
 * DynamoDB item representing the top-level metadata of a game.
 *
 * @see GameBoardDdbItem
 *
 * @author fridge
 */
@Data
@DynamoDBTable(tableName = GameDdbItem.TABLE_NAME)
public class GameDdbItem implements DdbItem {

    public static final String TABLE_NAME = "Games";

    public static final String GSI_CREATION_TIME = "GSI-TimeHr-TimeMs";
    public static final String GSI_GAME_NAME = "GSI-GameName";

    private static final String COL_GAME_ID = "GameID";
    private static final String COL_CREATION_TIME_HR = "CreationTimeHour";
    private static final String COL_CREATION_TIME_MS = "CreationTimeMillis";
    private static final String COL_FIRST_USER_ID = "FirstUserID";
    private static final String COL_SECOND_USER_ID = "SecondUserID";
    public static final String COL_STATUS = "Status";
    private static final String COL_GAME_NAME = "GameName";
    private static final String COL_RESULT = "Result";
    private static final String COL_WINNER_USER_ID = "WinnerUserID";
    private static final String COL_OPTIMISTIC_LOCKING_VERSION = "OptimisticLockVersion";

    /**
     * Unique ID of the game.
     */
    @DynamoDBHashKey(attributeName = COL_GAME_ID)
    private String gameId;

    /**
     * The truncated hour that the game was created in.
     */
    @DynamoDBIndexHashKey(attributeName = COL_CREATION_TIME_HR, globalSecondaryIndexName = GSI_CREATION_TIME)
    private long creationTimeHr;

    /**
     * The timestamp that the game was created in.
     */
    @DynamoDBIndexRangeKey(attributeName = COL_CREATION_TIME_MS, globalSecondaryIndexName = GSI_CREATION_TIME)
    private long creationTimeMs;

    /**
     * The first user to join the match. Assumed to be the host who created the match.
     */
    @DynamoDBAttribute(attributeName = COL_FIRST_USER_ID)
    private String firstUserId;

    /**
     * The second user to join the match. Assumed to be the guest.
     */
    @DynamoDBAttribute(attributeName = COL_SECOND_USER_ID)
    private String secondUserId;

    /**
     * The status of the game.
     */
    @DynamoDBTypeConvertedEnum
    @DynamoDBAttribute(attributeName = COL_STATUS)
    private GameStatusType status;

    /**
     * The name of the game, which allows players to search for a friend's game.
     */
    @DynamoDBIndexHashKey(attributeName = COL_GAME_NAME, globalSecondaryIndexName = GSI_GAME_NAME)
    private String gameName;

    /**
     * The result/outcome of the game.
     */
    @DynamoDBTypeConvertedEnum
    @DynamoDBAttribute(attributeName = COL_RESULT)
    private GameResultType result;

    /**
     * The user ID of the winner.
     */
    @DynamoDBAttribute(attributeName = COL_WINNER_USER_ID)
    private String winnerUserId;

    /**
     * Used for optimistic locking.
     */
    @DynamoDBVersionAttribute(attributeName = COL_OPTIMISTIC_LOCKING_VERSION)
    private Long optimisticLockingVersion;
}
