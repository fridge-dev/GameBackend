package com.frjgames.dal.ddb.accessors;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.frjgames.dal.ddb.utils.DdbExpressionFactory;
import com.frjgames.dal.ddb.items.UserDdbItem;
import com.frjgames.dal.exceptions.InvalidDataException;
import java.util.List;
import java.util.Optional;

/**
 * Accessor for user items.
 *
 * @author fridge
 */
public class UserDdbAccessor extends BaseDynamoDbAccessor<UserDdbItem> {

    public UserDdbAccessor(final DynamoDBMapper dbMapper) {
        super(dbMapper, UserDdbItem.class);
    }

    /**
     * Save a user as long as no other user has had that username.
     */
    public void createUser(final UserDdbItem item) {
        DynamoDBSaveExpression saveCondition = DdbExpressionFactory.newSaveExpressionItemDoesntExist(UserDdbItem.COL_USER_ID);

        super.saveItem(item, saveCondition);
    }

    /**
     * Loads user item from unique user ID
     */
    public Optional<UserDdbItem> loadUser(final String userId) {
        return super.loadItem(userId);
    }

    /**
     * Loads user item from username index (GSI).
     */
    public Optional<UserDdbItem> loadUserByUsername(final String username) {
        UserDdbItem key = new UserDdbItem();
        key.setUsername(username);

        DynamoDBQueryExpression<UserDdbItem> query = new DynamoDBQueryExpression<UserDdbItem>()
                .withIndexName(UserDdbItem.INDEX_USERNAME)
                .withHashKeyValues(key)
                .withConsistentRead(false);

        QueryResultPage<UserDdbItem> resultPage = super.querySinglePage(query);
        List<UserDdbItem> results = resultPage.getResults();

        if (results.size() == 1) {
            return Optional.of(results.get(0));
        } else if (results.size() == 0) {
            return Optional.empty();
        } else {
            throw new InvalidDataException(String.format("Multiple users with username '%s'.", username));
        }
    }
}
