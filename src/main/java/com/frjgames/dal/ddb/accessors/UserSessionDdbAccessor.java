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
     * Put a new session.
     */
    public void createOrUpdate(final UserSessionDdbItem item) {
        super.saveItem(item);
    }

    /**
     * Loads session item from unique user ID
     */
    public Optional<UserSessionDdbItem> load(final String userId) {
        return super.loadItem(userId);
    }

}
