package com.frjgames.dal.accessors;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.frjgames.dal.ddb.accessors.DynamoDbAccessor;
import com.frjgames.dal.ddb.items.UserDdbItem;
import com.frjgames.dal.ddb.utils.DdbExceptionTranslator;
import com.frjgames.dal.ddb.utils.DdbExpressionFactory;
import com.frjgames.dal.models.data.User;
import com.frjgames.dal.models.interfaces.UserAccessor;
import com.frjgames.dal.models.keys.UserDataKey;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

/**
 * Database accessor abstraction for User data.
 *
 * NOTE: This is the layer where we our vocabulary maps username <-> userId
 *
 * @author fridge
 */
@RequiredArgsConstructor
public class UserAccessorImplV2 implements UserAccessor {

    private static final String CREATE_USER_CONDITION_FAILED_MESSAGE = "Failed to create user because username is already taken.";

    private final DynamoDbAccessor<UserDdbItem> ddbAccessor;

    /**
     * Create new unique user using username as the user ID.
     */
    @Override
    public void create(final User user) {
        UserDdbItem item = domainTypeToItem(user);
        DynamoDBSaveExpression saveCondition = DdbExpressionFactory.newSaveExpressionItemDoesntExist(UserDdbItem.COL_USER_ID);

        DdbExceptionTranslator.conditionalWrite(
                () -> ddbAccessor.saveItem(item, saveCondition),
                CREATE_USER_CONDITION_FAILED_MESSAGE
        );
    }

    private UserDdbItem domainTypeToItem(final User user) {
        UserDdbItem item = new UserDdbItem();

        item.setUserId(user.getUsername());
        item.setPassword(user.getPassword());

        return item;
    }

    /**
     * Load user by the unique username (i.e. user ID).
     */
    @Override
    public Optional<User> load(final UserDataKey key) {
        return ddbAccessor.loadItem(key.getUsername())
                .map(this::itemToDomainType);
    }

    private User itemToDomainType(final UserDdbItem item) {
        return User.builder()
                .username(item.getUserId())
                .password(item.getPassword())
                .build();
    }
}
