package com.frjgames.dal.ddb.items;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Data;

/**
 * DynamoDB item of a user session.
 *
 * @author fridge
 */
@Data
@DynamoDBTable(tableName = UserSessionDdbItem.TABLE_NAME)
public class UserSessionDdbItem implements DdbItem {

    public static final String TABLE_NAME = "UserSessions";

    public static final String COL_USER_ID = "UserID";
    private static final String COL_SESSION_ID = "SessionID";
    private static final String COL_EXPIRATION_TIMESTAMP_MS = "ExpirationTimestampMillis";

    /**
     * Unique user ID.
     */
    @DynamoDBHashKey(attributeName = COL_USER_ID)
    private String userId;

    /**
     * Unique session ID of the user. Only one session per user.
     */
    @DynamoDBAttribute(attributeName = COL_SESSION_ID)
    private String sessionId;

    /**
     * Time at which the session is no longer valid.
     */
    @DynamoDBAttribute(attributeName = COL_EXPIRATION_TIMESTAMP_MS)
    private long expirationTimestampMs;

}
