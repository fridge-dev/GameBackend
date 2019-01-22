package com.frjgames.dal.accessors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.frjgames.dal.ddb.items.UserDdbItem;
import com.frjgames.dal.ddb.testutils.TestUtilDynamoDbLocalTestBase;
import com.frjgames.dal.models.exceptions.InvalidDataException;
import com.frjgames.dal.models.interfaces.UserAccessor;
import com.frjgames.dal.models.data.User;
import com.frjgames.dal.models.keys.UserDataKey;
import com.frjgames.testutils.TestUtilExceptionValidator;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;

/**
 * Functional integ test for {@link UserAccessorImplV2} class.
 *
 * @author fridge
 */
public class UserAccessorImplV2Test extends TestUtilDynamoDbLocalTestBase<UserDdbItem> {

    private static final String USER_ID = "souhgaougaasgunjs9hf184";
    private static final String USERNAME = "fridge";
    private static final String PASSWORD = "H^g97R%vk,";

    private UserAccessor accessor;

    public UserAccessorImplV2Test() {
        super(UserDdbItem.class);
    }

    @Before
    public void setup() {
        accessor = getModule().userAccessor();
    }

    @Test
    public void createUser() throws Exception {
        User user = newUser();

        accessor.create(user);
    }

    @Test
    public void createUser_SameId() throws Exception {
        User user = newUser();

        accessor.create(user);

        TestUtilExceptionValidator.validateThrown(ConditionalCheckFailedException.class, () -> accessor.create(user));
    }

    @Test
    public void createUser_SameUsername() throws Exception {
        User user1 = newUser();

        User user2 = user1.toBuilder()
                .userId("otherId")
                .build();

        // It is allowed
        accessor.create(user1);
        accessor.create(user2);
    }

    @Test
    public void loadUserByUsername() throws Exception {
        User savedUser = newUser();
        UserDataKey userKey = UserDataKey.builder()
                .username(savedUser.getUsername())
                .build();

        // 1. Load
        assertFalse(accessor.load(userKey).isPresent());

        // 2. Create
        accessor.create(savedUser);

        // 3. Load (again)
        Optional<User> loadedUser = accessor.load(userKey);

        assertEquals(savedUser, loadedUser.orElse(null));
    }

    @Test(expected = InvalidDataException.class)
    public void loadUserByUsername_MultipleUsers() throws Exception {
        // Save two users with same name
        User user1 = newUser();
        User user2 = user1.toBuilder()
                .userId("otherId")
                .build();

        accessor.create(user1);
        accessor.create(user2);

        // Load by user name
        accessor.load(UserDataKey.builder().username(user1.getUsername()).build());
    }

    private User newUser() {
        return User.builder()
                .userId(USER_ID)
                .username(USERNAME)
                .password(PASSWORD)
                .build();
    }

}