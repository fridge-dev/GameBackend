package com.frjgames.dal.ddb.items;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Data;

/**
 * DynamoDB item representing a user.
 *
 * @author fridge
 */
@Data
@DynamoDBTable(tableName = UserDdbItem.TABLE_NAME)
public class UserDdbItem implements DdbItem {

    public static final String TABLE_NAME = "Users";
    public static final String INDEX_USERNAME = "GSI-Username";

    public static final String COL_USER_ID = "UserID";
    private static final String COL_USERNAME = "Username";
    private static final String COL_PASSWORD = "Password";

    /**
     * Unique user ID.
     */
    @DynamoDBHashKey(attributeName = COL_USER_ID)
    private String userId;

    /**
     * User's display name.
     */
    @DynamoDBAttribute(attributeName = COL_USERNAME)
    @DynamoDBIndexHashKey(globalSecondaryIndexName = INDEX_USERNAME)
    private String username;

    /**
     * Encrypted password.
     */
    @DynamoDBAttribute(attributeName = COL_PASSWORD)
    private String password;

}
