package com.frjgames.dal.ddb.accessors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import com.frjgames.dal.ddb.items.UserSessionDdbItem;
import com.frjgames.dal.ddb.testutils.TestUtilDynamoDbAccessorTestBase;
import java.util.Optional;
import org.junit.Test;

/**
 * Tests the {@link UserSessionDdbAccessor} class.
 *
 * @author alecva
 */
public class UserSessionDdbAccessorTest extends TestUtilDynamoDbAccessorTestBase<UserSessionDdbItem, UserSessionDdbAccessor> {

    private static final String USER_ID = "user-123";
    private static final String SESSION_ID = "session-7654";
    private static final long EXPIRY = 12342345L;

    public UserSessionDdbAccessorTest() {
        super(UserSessionDdbItem.class, UserSessionDdbAccessor::new);
    }

    @Test
    public void createOrUpdate_Create() throws Exception {
        UserSessionDdbItem item = newItem();

        accessor.createOrUpdate(item);
    }

    @Test
    public void createOrUpdate_CreateThenUpdate() throws Exception {
        UserSessionDdbItem item = newItem();

        accessor.createOrUpdate(item);

        item.setSessionId(SESSION_ID + "newer-session");
        item.setExpirationTimestampMs(EXPIRY + 1234L);

        // It is allowed
        accessor.createOrUpdate(item);
    }

    @Test
    public void load() throws Exception {
        UserSessionDdbItem item = newItem();

        // Load nothing, create, then load
        assertFalse(accessor.load(item.getUserId()).isPresent());

        accessor.createOrUpdate(item);

        Optional<UserSessionDdbItem> loadedItem = accessor.load(item.getUserId());
        assertEquals(item, loadedItem.orElse(null));
    }

    private UserSessionDdbItem newItem() {
        UserSessionDdbItem item = new UserSessionDdbItem();
        item.setUserId(USER_ID);
        item.setSessionId(SESSION_ID);
        item.setExpirationTimestampMs(EXPIRY);
        return item;
    }
}