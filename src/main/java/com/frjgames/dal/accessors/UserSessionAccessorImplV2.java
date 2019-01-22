package com.frjgames.dal.accessors;

import com.frjgames.dal.ddb.accessors.DynamoDbAccessor;
import com.frjgames.dal.ddb.items.UserSessionDdbItem;
import com.frjgames.dal.models.interfaces.UserSessionAccessor;
import com.frjgames.dal.models.data.PersistedUserSession;
import com.frjgames.dal.models.keys.UserSessionDataKey;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

/**
 * Data accessor for Session data.
 *
 * @author fridge
 */
@RequiredArgsConstructor
public class UserSessionAccessorImplV2 implements UserSessionAccessor {

    private final DynamoDbAccessor<UserSessionDdbItem> ddbAccessor;

    /**
     * "Create" doesn't enforce no session already exists. It will overwrite any existing session.
     *
     * In the future, consider moving this to a createOrUpdate method in {@link UserSessionAccessor}.
     */
    @Override
    public void create(final PersistedUserSession persistedUserSession) {
        ddbAccessor.saveItem(domainTypeToItem(persistedUserSession));
    }

    private UserSessionDdbItem domainTypeToItem(final PersistedUserSession persistedUserSession) {
        UserSessionDdbItem item = new UserSessionDdbItem();
        item.setUserId(persistedUserSession.getUserId());
        item.setSessionId(persistedUserSession.getSessionId());
        item.setExpirationTimestampMs(persistedUserSession.getExpiryTimestampMs());

        return item;
    }

    /**
     * Loads the session data from unique user ID.
     */
    @Override
    public Optional<PersistedUserSession> load(final UserSessionDataKey key) {
        return ddbAccessor.loadItem(key.getUserId())
                .map(this::itemToDomainType);
    }

    private PersistedUserSession itemToDomainType(final UserSessionDdbItem sessionDdbItem) {
        return PersistedUserSession.builder()
                .userId(sessionDdbItem.getUserId())
                .sessionId(sessionDdbItem.getSessionId())
                .expiryTimestampMs(sessionDdbItem.getExpirationTimestampMs())
                .build();
    }
}
