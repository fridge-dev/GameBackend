package com.mycompany.app.frj.dal.impl;

import static org.junit.Assert.assertEquals;

import com.mycompany.app.frj.dal.config.TestDataAccessConfig;
import com.mycompany.app.frj.dal.ddb.items.UserSessionDdbItem;
import com.mycompany.app.frj.dal.interfaces.UserSessionAccessor;
import com.mycompany.app.frj.dal.models.UserSession;
import com.mycompany.app.frj.dal.models.keys.UserSessionDataAccessKey;
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
        TestDataAccessConfig config = new TestDataAccessConfig();
        config.createTable(UserSessionDdbItem.class);

        userSessionAccessor = config.userSessionAccessor();
    }

    @Test
    public void createThenLoad() throws Exception {
        UserSession userSession = UserSession.builder()
                .userId(USER_ID)
                .sessionId(SESSION_ID)
                .expiryTimestamp(EXPIRY)
                .build();
        UserSessionDataAccessKey userSessionKey = UserSessionDataAccessKey.builder()
                .userId(userSession.getUserId())
                .build();

        userSessionAccessor.create(userSession);

        Optional<UserSession> loadedSession = userSessionAccessor.load(userSessionKey);

        assertEquals(userSession, loadedSession.get());
    }
}