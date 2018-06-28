package com.mycompany.app.frj.dal.ddb.accessors;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.google.common.collect.ImmutableMap;
import com.mycompany.app.frj.dal.ddb.items.UserDdbItem;
import com.mycompany.app.frj.dal.exceptions.InvalidDataException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Accessor for user items.
 *
 * @author alecva
 */
public class UserDdbAccessor extends BaseDynamoDbAccessor<UserDdbItem> {

    public UserDdbAccessor(final DynamoDBMapper dbMapper) {
        super(dbMapper, UserDdbItem.class);
    }

    /**
     * Save a user as long as no other user has had that username.
     */
    public void createUser(final UserDdbItem item) {
        Map<String, ExpectedAttributeValue> expectUserDoesntExist = ImmutableMap.of(UserDdbItem.COL_USER_ID, new ExpectedAttributeValue(false));

        DynamoDBSaveExpression saveCondition = new DynamoDBSaveExpression().withExpected(expectUserDoesntExist);

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
