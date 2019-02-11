package com.frjgames.app.internal.sessions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.frjgames.dal.models.interfaces.UserSessionAccessor;
import com.frjgames.dal.models.data.PersistedUserSession;
import com.frjgames.app.internal.sessions.models.CreateSessionInput;
import com.frjgames.app.internal.sessions.models.SessionData;
import com.frjgames.dal.models.keys.UserSessionDataKey;
import com.frjgames.testutils.argmatcher.TestUtilArgMatcher;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Tests the {@link RngSessionManager} class.
 *
 * @author fridge
 */
@RunWith(MockitoJUnitRunner.class)
public class RngSessionManagerTest {

    private static final String USER_ID = "user-1234";
    private static final String SESSION_ID = "a09s7gf0q2oi4uhf12f";

    @InjectMocks
    private RngSessionManager rngSessionManager;

    @Mock
    private UserSessionAccessor injectedUserSessionAccessor;

    @Test
    public void createSession() throws Exception {
        CreateSessionInput input = CreateSessionInput.builder()
                .userId(USER_ID)
                .build();

        SessionData returnedSession = rngSessionManager.createSession(input);

        ArgumentCaptor<PersistedUserSession> captor = ArgumentCaptor.forClass(PersistedUserSession.class);
        verify(injectedUserSessionAccessor).create(captor.capture());

        PersistedUserSession persistedSession = captor.getValue();
        assertEquals(USER_ID, persistedSession.getUserId());
        assertEquals(USER_ID, returnedSession.getUserId());
        assertEquals(persistedSession.getExpiryTimestampMs(), returnedSession.getExpirationTimestampMs());
        assertTrue(persistedSession.getExpiryTimestampMs() > now());
    }

    /**
     * Generate a large number of sessions, and verify each was unique.
     *
     * Technically, RNG could cause this test to innocently fail, however the chances should be sufficiently
     * low (< 0.01%), so we are comfortable testing a large data set.
     */
    @Test
    public void createSession_StressTestMultipleUnique() throws Exception {
        // Arbitrarily large numbers.
        int numUsers = 100;
        int numSessions = 300;

        Set<String> uniqueSessions = new HashSet<>();
        for (int i = 0; i < numUsers; i++) {
            CreateSessionInput input = CreateSessionInput.builder()
                    .userId("user-" + i)
                    .build();

            for (int j = 0; j < numSessions; j++) {
                SessionData session = rngSessionManager.createSession(input);
                uniqueSessions.add(session.getPublicSessionToken());
            }
        }

        assertEquals(numUsers * numSessions, uniqueSessions.size());
    }

    private void stubSessionAccessorLoad(final String sessionId, final long expiration) {
        PersistedUserSession mockServerSession = PersistedUserSession.builder()
                .userId(USER_ID)
                .sessionId(sessionId)
                .expiryTimestampMs(expiration)
                .build();

        when(injectedUserSessionAccessor.load(argThat(matchesUserId(USER_ID)))).thenReturn(Optional.of(mockServerSession));
    }

    private void stubSessionAccessorLoadNothing() {
        when(injectedUserSessionAccessor.load(argThat(matchesUserId(USER_ID)))).thenReturn(Optional.empty());
    }

    private Matcher<UserSessionDataKey> matchesUserId(final String userId) {
        return new TestUtilArgMatcher<>(invoked -> userId.equals(invoked.getUserId()));
    }

    @Test
    public void isValidSession_Valid() throws Exception {
        stubSessionAccessorLoad(SESSION_ID, now() + 100000L);

        SessionData clientSession = SessionData.builder()
                .userId(USER_ID)
                .publicSessionToken(USER_ID + "::" + SESSION_ID)
                .expirationTimestampMs(now() + 100000L)
                .build();

        assertTrue(rngSessionManager.isValidSession(clientSession));
    }

    @Test
    public void isValidSession_NotValid_ClientTimeExpired() throws Exception {
        SessionData clientSession = SessionData.builder()
                .userId(USER_ID)
                .publicSessionToken(USER_ID + "::" + SESSION_ID)
                .expirationTimestampMs(now() - 100000L)
                .build();

        assertFalse(rngSessionManager.isValidSession(clientSession));
    }

    @Test
    public void isValidSession_NotValid_SessionDoesntExist() throws Exception {
        stubSessionAccessorLoadNothing();

        SessionData clientSession = SessionData.builder()
                .userId(USER_ID)
                .publicSessionToken(USER_ID + "::" + SESSION_ID)
                .expirationTimestampMs(now() + 100000L)
                .build();

        assertFalse(rngSessionManager.isValidSession(clientSession));
    }

    @Test
    public void isValidSession_NotValid_EncodedUserDoesntMatch() throws Exception {
        stubSessionAccessorLoad(SESSION_ID, now() + 100000L);

        SessionData clientSession = SessionData.builder()
                .userId(USER_ID)
                .publicSessionToken("wrong" + USER_ID + "::" + SESSION_ID)
                .expirationTimestampMs(now() + 100000L)
                .build();

        assertFalse(rngSessionManager.isValidSession(clientSession));
    }

    @Test
    public void isValidSession_NotValid_SessionIdDoesntMatch() throws Exception {
        stubSessionAccessorLoad(SESSION_ID, now() + 100000L);

        SessionData clientSession = SessionData.builder()
                .userId(USER_ID)
                .publicSessionToken(USER_ID + "::" + SESSION_ID + "wrong")
                .expirationTimestampMs(now() + 100000L)
                .build();

        assertFalse(rngSessionManager.isValidSession(clientSession));
    }

    @Test
    public void isValidSession_NotValid_ServerTimeExpired() throws Exception {
        stubSessionAccessorLoad(SESSION_ID, now() - 100000L);

        SessionData clientSession = SessionData.builder()
                .userId(USER_ID)
                .publicSessionToken(USER_ID + "::" + SESSION_ID)
                .expirationTimestampMs(now() + 100000L)
                .build();

        assertFalse(rngSessionManager.isValidSession(clientSession));
    }

    private long now() {
        return System.currentTimeMillis();
    }

}