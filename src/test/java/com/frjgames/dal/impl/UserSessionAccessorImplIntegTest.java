package com.frjgames.dal.impl;

import static org.junit.Assert.assertEquals;

import com.frjgames.dal.config.TestDataAccessConfig;
import com.frjgames.dal.interfaces.UserSessionAccessor;
import com.frjgames.dal.models.PersistedUserSession;
import com.frjgames.dal.models.keys.UserSessionDataKey;
import com.frjgames.dal.ddb.items.UserSessionDdbItem;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;

/**
 * Functional integration test of the {@link UserSessionAccessorImpl} class.
 *
 * @author alecva
 */
public class UserSessionAccessorImplIntegTest {

    private static final String USER_ID = "user-123";
    private static final String SESSION_ID = "session-7654";
    private static final long EXPIRY = 12342345L;

    private UserSessionAccessor userSessionAccessor;

    @Before
    public void setup() {
        TestDataAccessConfig config = new TestDataAccessConfig(UserSessionDdbItem.class);

        userSessionAccessor = config.userSessionAccessor();
    }

    @Test
    public void createThenLoad() throws Exception {
        PersistedUserSession userSession = PersistedUserSession.builder()
                .userId(USER_ID)
                .sessionId(SESSION_ID)
                .expiryTimestampMs(EXPIRY)
                .build();
        UserSessionDataKey userSessionKey = UserSessionDataKey.builder()
                .userId(userSession.getUserId())
                .build();

        userSessionAccessor.create(userSession);

        Optional<PersistedUserSession> loadedSession = userSessionAccessor.load(userSessionKey);

        assertEquals(userSession, loadedSession.get());
    }
}