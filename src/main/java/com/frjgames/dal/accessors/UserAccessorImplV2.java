package com.frjgames.dal.accessors;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.frjgames.dal.ddb.accessors.DynamoDbAccessor;
import com.frjgames.dal.ddb.items.UserDdbItem;
import com.frjgames.dal.ddb.utils.DdbExpressionFactory;
import com.frjgames.dal.models.exceptions.InvalidDataException;
import com.frjgames.dal.models.interfaces.UserAccessor;
import com.frjgames.dal.models.data.User;
import com.frjgames.dal.models.keys.UserDataKey;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

/**
 * Database accessor abstraction for User data.
 *
 * @author fridge
 */
@RequiredArgsConstructor
public class UserAccessorImplV2 implements UserAccessor {

    private final DynamoDbAccessor<UserDdbItem> ddbAccessor;

    /**
     * Create new unique user ID.
     */
    @Override
    public void create(final User user) {
        UserDdbItem item = domainTypeToItem(user);
        DynamoDBSaveExpression saveCondition = DdbExpressionFactory.newSaveExpressionItemDoesntExist(UserDdbItem.COL_USER_ID);

        ddbAccessor.saveItem(item, saveCondition);
    }

    private UserDdbItem domainTypeToItem(final User user) {
        UserDdbItem item = new UserDdbItem();

        item.setUserId(user.getUserId());
        item.setUsername(user.getUsername());
        item.setPassword(user.getPassword());

        return item;
    }

    /**
     * Load user by username (GSI).
     */
    @Override
    public Optional<User> load(UserDataKey key) {
        return loadUserByUsername(key.getUsername())
                .map(this::itemToDomainType);
    }

    private Optional<UserDdbItem> loadUserByUsername(final String username) {
        UserDdbItem key = new UserDdbItem();
        key.setUsername(username);

        DynamoDBQueryExpression<UserDdbItem> query = new DynamoDBQueryExpression<UserDdbItem>()
                .withIndexName(UserDdbItem.INDEX_USERNAME)
                .withHashKeyValues(key)
                .withConsistentRead(false);

        QueryResultPage<UserDdbItem> resultPage = ddbAccessor.querySinglePage(query);
        List<UserDdbItem> results = resultPage.getResults();

        if (results.size() == 1) {
            return Optional.of(results.get(0));
        } else if (results.size() == 0) {
            return Optional.empty();
        } else {
            throw new InvalidDataException(String.format("Multiple users with username '%s'.", username));
        }
    }

    private User itemToDomainType(final UserDdbItem item) {
        return User.builder()
                .username(item.getUsername())
                .userId(item.getUserId())
                .password(item.getPassword())
                .build();
    }
}
