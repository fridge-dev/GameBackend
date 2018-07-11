package com.mycompany.app.frj.app.sessions;

import com.mycompany.app.frj.app.sessions.models.CreateSessionInput;
import com.mycompany.app.frj.app.sessions.models.SessionData;
import com.mycompany.app.frj.dal.interfaces.UserSessionAccessor;
import com.mycompany.app.frj.dal.models.PersistedUserSession;
import com.mycompany.app.frj.dal.models.keys.UserSessionDataKey;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;

/**
 * An implementation of a session manager which uses a long, randomly generated session ID as a means of security.
 *
 * It is based on the below research.
 *
 * - https://security.stackexchange.com/a/72838/181385
 * - https://security.stackexchange.com/questions/20129/how-and-when-do-i-use-hmac/20301
 * - https://stackoverflow.com/a/20168610/6170935
 *
 * This can likely be improved upon, when the time comes. HMAC should be considered for generating the session token.
 *
 * @author alecva
 */
@RequiredArgsConstructor
public class RngSessionManager implements SessionManager {

    /**
     * 26 + 26 + 10 = 62 alphabet size
     *
     * 62^32 = 2^190 possibilities
     */
    private static final String SESSION_ID_ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int SESSION_ID_SIZE = 32;
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * To avoid customer churn, consider increasing this to a larger number.
     */
    private static final long SESSION_DURATION_MS = TimeUnit.HOURS.toMillis(12);

    private final UserSessionAccessor userSessionAccessor;

    /**
     * Create a random session ID that expires in a fixed amount of time.
     */
    @Override
    public SessionData createSession(final CreateSessionInput input) {
        PersistedUserSession persistedUserSession = PersistedUserSession.builder()
                .userId(input.getUserId())
                .sessionId(newRandomSessionId())
                .expiryTimestampMs(newExpiryTimestampMs())
                .build();

        userSessionAccessor.create(persistedUserSession);

        return encodeSessionData(persistedUserSession);
    }

    /**
     * Very inefficient, but it works for now.
     */
    private String newRandomSessionId() {
        char[] sessionId = new char[SESSION_ID_SIZE];

        for (int i = 0; i < SESSION_ID_SIZE; i++) {
            sessionId[i] = newRandomSessionChar();
        }

        return String.valueOf(sessionId);
    }

    private char newRandomSessionChar() {
        int i = RANDOM.nextInt(SESSION_ID_ALPHABET.length());
        return SESSION_ID_ALPHABET.charAt(i);
    }

    private long newExpiryTimestampMs() {
        return timestampNowMs() + SESSION_DURATION_MS;
    }

    private long timestampNowMs() {
        return System.currentTimeMillis();
    }

    /**
     * @inheritDoc
     *
     * @return true if the provided session is valid and current.
     */
    @Override
    public boolean isValidSession(final SessionData clientProvidedSession) {
        // Fail fast, for good behaving clients (a majority of clients)
        if (clientProvidedSession.getExpirationTimestampMs() < timestampNowMs()) {
            return false;
        }

        // Decode and validate session
        PersistedUserSession decodedClientSession = decodeSessionData(clientProvidedSession.getPublicSessionToken(), clientProvidedSession.getExpirationTimestampMs());
        if (!clientProvidedSession.getUserId().equals(decodedClientSession.getUserId())) {
            return false;
        }

        // Load session from DB and validate all the fields.
        UserSessionDataKey sessionDataKey = UserSessionDataKey.builder()
                .userId(decodedClientSession.getUserId())
                .build();
        Optional<PersistedUserSession> persistedSession = userSessionAccessor.load(sessionDataKey);

        return persistedSession.isPresent()
                && clientSessionMatchesServerSession(decodedClientSession, persistedSession.get());

    }

    /**
     * IMPORTANT: Client can easily forge timestamp, so we should only consider DB's expiration timestamp.
     */
    private boolean clientSessionMatchesServerSession(final PersistedUserSession clientSession, final PersistedUserSession serverSession) {
        // We always expect user ID to match, since it is loaded from DB using the client's provided
        // user ID. We still validate it for sanity's sake.
        boolean userMatches = serverSession.getUserId().equals(clientSession.getUserId());
        boolean sessionIdMatches = serverSession.getSessionId().equals(clientSession.getSessionId());
        boolean isNotExpired = serverSession.getExpiryTimestampMs() > timestampNowMs();

        return userMatches && sessionIdMatches && isNotExpired;
    }

    private SessionData encodeSessionData(final PersistedUserSession persistedUserSession) {
        String encodedSessionId = String.format("%s::%s", persistedUserSession.getUserId(), persistedUserSession.getSessionId());

        return SessionData.builder()
                .publicSessionToken(encodedSessionId)
                .userId(persistedUserSession.getUserId())
                .expirationTimestampMs(persistedUserSession.getExpiryTimestampMs())
                .build();
    }

    private PersistedUserSession decodeSessionData(final String sessionToken, final long expirationTimestampMs) {
        String[] split = sessionToken.split("::");
        String userId = split[0];
        String sessionId = split[1];

        return PersistedUserSession.builder()
                .userId(userId)
                .sessionId(sessionId)
                .expiryTimestampMs(expirationTimestampMs)
                .build();
    }
}
