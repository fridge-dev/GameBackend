package com.frjgames.dal.accessors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import com.frjgames.dal.ddb.items.UserDdbItem;
import com.frjgames.dal.ddb.testutils.TestUtilDynamoDbLocalTestBase;
import com.frjgames.dal.models.data.User;
import com.frjgames.dal.models.exceptions.ConditionalWriteException;
import com.frjgames.dal.models.interfaces.UserAccessor;
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

    private static final String USERNAME = "fridge";
    private static final String PASSWORD = "H^g97R%vk,";

    private UserAccessor accessor;

    public UserAccessorImplV2Test() {
        super(UserDdbItem.class);
    }

    @Before
    public void setup() {
        accessor = getDalModule().userAccessor();
    }

    @Test
    public void create() throws Exception {
        User user = newUser();

        accessor.create(user);
    }

    @Test
    public void create_SameUsername() throws Exception {
        User user = newUser();

        accessor.create(user);

        TestUtilExceptionValidator.validateThrown(ConditionalWriteException.class, () -> accessor.create(user));
    }

    @Test
    public void load() throws Exception {
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

    private User newUser() {
        return User.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .build();
    }

}