package com.frjgames.dal.accessors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import com.frjgames.dal.ddb.items.UserSessionDdbItem;
import com.frjgames.dal.ddb.testutils.TestUtilDynamoDbLocalTestBase;
import com.frjgames.dal.models.interfaces.UserSessionAccessor;
import com.frjgames.dal.models.data.PersistedUserSession;
import com.frjgames.dal.models.keys.UserSessionDataKey;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;

/**
 * Functional integ tests the {@link UserSessionAccessorImplV2} class.
 *
 * @author fridge
 */
public class UserSessionAccessorImplV2Test extends TestUtilDynamoDbLocalTestBase<UserSessionDdbItem> {

    private static final String USER_ID = "user-123";
    private static final String SESSION_ID = "session-7654";
    private static final long EXPIRY = 12342345L;

    private UserSessionAccessor accessor;

    public UserSessionAccessorImplV2Test() {
        super(UserSessionDdbItem.class);
    }

    @Before
    public void setup() {
        accessor = getModule().userSessionAccessor();
    }

    @Test
    public void createOrUpdate_Create() throws Exception {
        PersistedUserSession session = newSession();

        accessor.create(session);
    }

    @Test
    public void createOrUpdate_CreateThenUpdate() throws Exception {
        PersistedUserSession session = newSession();

        accessor.create(session);

        session = session.toBuilder()
                .sessionId(SESSION_ID + "newer-session")
                .expiryTimestampMs(EXPIRY + 1234L)
                .build();

        // It is allowed
        accessor.create(session);
    }

    @Test
    public void load() throws Exception {
        PersistedUserSession savedSession = newSession();
        UserSessionDataKey key = UserSessionDataKey.builder()
                .userId(savedSession.getUserId())
                .build();

        // 1. Load
        assertFalse(accessor.load(key).isPresent());

        // 2. Create
        accessor.create(savedSession);

        // 3. Load (again
        Optional<PersistedUserSession> loadedSession = accessor.load(key);
        assertEquals(savedSession, loadedSession.orElse(null));
    }

    private PersistedUserSession newSession() {
        return PersistedUserSession.builder()
                .userId(USER_ID)
                .sessionId(SESSION_ID)
                .expiryTimestampMs(EXPIRY)
                .build();
    }
}