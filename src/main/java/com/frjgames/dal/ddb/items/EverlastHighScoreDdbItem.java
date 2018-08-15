package com.frjgames.dal.ddb.items;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.frjgames.dal.ddb.typeconverters.converters.WorldLevelDdbTypeConverter;
import com.frjgames.dal.ddb.typeconverters.types.WorldLevelDdbType;
import lombok.Data;

/**
 * DynamoDB item representing a high score in the Everlast game.
 *
 * @author fridge
 */
@Data
@DynamoDBTable(tableName = EverlastHighScoreDdbItem.TABLE_NAME)
public class EverlastHighScoreDdbItem implements DdbItem {

    public static final String TABLE_NAME = "Everlast-HighScores";
    public static final String INDEX_LEVEL_ID_SCORE = "GSI-LevelID-Score";

    private static final String COL_USERNAME = "Username";
    private static final String COL_PASSWORD = "Password";
    private static final String COL_LEVEL_ID = "LevelID";
    private static final String COL_SCORE = "LevelScore";
    private static final String COL_GHOST_DATA = "GhostData";
    private static final String COL_TIMESTAMP = "Timestamp";

    /**
     * User's display name.
     */
    @DynamoDBAttribute(attributeName = COL_USERNAME)
    private String username;

    /**
     * The unique identifier of the level.
     */
    @DynamoDBRangeKey(attributeName = COL_LEVEL_ID)
    @DynamoDBTypeConverted(converter = WorldLevelDdbTypeConverter.class)
    @DynamoDBIndexHashKey(globalSecondaryIndexName = INDEX_LEVEL_ID_SCORE)
    private WorldLevelDdbType levelId;

    /**
     * The numeric score of the high score.
     */
    @DynamoDBAttribute(attributeName = COL_SCORE)
    @DynamoDBIndexRangeKey(globalSecondaryIndexName = INDEX_LEVEL_ID_SCORE)
    private int score;

    /**
     * The encoded data of the high score's ghost.
     */
    @DynamoDBAttribute(attributeName = COL_GHOST_DATA)
    private String ghostData;

    /**
     * The timestamp at which this ghost was created.
     */
    @DynamoDBAttribute(attributeName = COL_TIMESTAMP)
    private long timestamp;

}
