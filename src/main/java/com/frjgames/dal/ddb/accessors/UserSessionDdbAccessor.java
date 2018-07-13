package com.frjgames.dal.ddb.accessors;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.frjgames.dal.ddb.items.UserSessionDdbItem;
import java.util.Optional;

/**
 * DynamoDB Accessor for accessing session table.
 *
 * @author fridge
 */
public class UserSessionDdbAccessor extends BaseDynamoDbAccessor<UserSessionDdbItem> {

    public UserSessionDdbAccessor(DynamoDBMapper dbMapper) {
        super(dbMapper, UserSessionDdbItem.class);
    }

    /**
     * Save a user as long as no other user has had that username.
     */
    public void save(final UserSessionDdbItem item) {
        super.saveItem(item);
    }

    /**
     * Loads user item from unique user ID
     */
    public Optional<UserSessionDdbItem> load(final String userId) {
        return super.loadItem(userId);
    }

}
