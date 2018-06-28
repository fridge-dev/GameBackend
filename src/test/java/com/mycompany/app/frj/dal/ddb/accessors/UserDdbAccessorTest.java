package com.mycompany.app.frj.dal.ddb.accessors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.mycompany.app.frj.dal.ddb.items.UserDdbItem;
import com.mycompany.app.frj.dal.ddb.testutils.TestUtilDynamoDbAccessorTestBase;
import com.mycompany.app.frj.dal.exceptions.InvalidDataException;
import java.util.Optional;
import org.junit.Test;

/**
 * Tests the {@link UserDdbAccessor} class.
 *
 * @author alecva
 */
public class UserDdbAccessorTest extends TestUtilDynamoDbAccessorTestBase<UserDdbItem, UserDdbAccessor> {

    private static final String USERNAME = "name-1";
    private static final String ID = "user-1";
    private static final String PASSWORD = "password-1";

    public UserDdbAccessorTest() {
        super(UserDdbItem.class, UserDdbAccessor::new);
    }

    @Test
    public void createUser() throws Exception {
        UserDdbItem user = newItem();

        accessor.createUser(user);
    }

    @Test
    public void createUser_SameId() throws Exception {
        UserDdbItem user = newItem();

        accessor.createUser(user);

        try {
            accessor.createUser(user);
            fail();
        } catch (ConditionalCheckFailedException e) {
            // Expected
        }
    }

    @Test
    public void createUser_SameUsername() throws Exception {
        UserDdbItem user1 = newItem();

        UserDdbItem user2 = newItem();
        user2.setUserId("otherId");

        // It is allowed
        accessor.createUser(user1);
        accessor.createUser(user2);
    }

    @Test
    public void loadUser() throws Exception {
        UserDdbItem user = newItem();

        // Load nothing, create, then load
        assertFalse(accessor.loadUser(user.getUserId()).isPresent());
        accessor.saveItem(user);
        Optional<UserDdbItem> output = accessor.loadUser(user.getUserId());

        assertEquals(user, output.get());
    }

    @Test
    public void loadUserByUsername() throws Exception {
        UserDdbItem user = newItem();

        // Load nothing, create, then load
        assertFalse(accessor.loadUserByUsername(user.getUsername()).isPresent());
        accessor.createUser(user);
        Optional<UserDdbItem> output = accessor.loadUserByUsername(user.getUsername());

        assertEquals(user, output.get());
    }

    @Test(expected = InvalidDataException.class)
    public void loadUserByUsername_MultipleUsers() throws Exception {
        // Save two users with same name
        UserDdbItem user = newItem();
        accessor.createUser(user);
        user.setUserId("otherId");
        accessor.createUser(user);

        // Load by user name
        accessor.loadUserByUsername(user.getUsername());
    }

    private UserDdbItem newItem() {
        UserDdbItem item = new UserDdbItem();
        item.setUserId(ID);
        item.setUsername(USERNAME);
        item.setPassword(PASSWORD);

        return item;
    }
}