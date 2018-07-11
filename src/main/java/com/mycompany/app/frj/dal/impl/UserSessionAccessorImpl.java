package com.mycompany.app.frj.dal.impl;

import com.mycompany.app.frj.dal.ddb.accessors.UserSessionDdbAccessor;
import com.mycompany.app.frj.dal.ddb.items.UserSessionDdbItem;
import com.mycompany.app.frj.dal.interfaces.UserSessionAccessor;
import com.mycompany.app.frj.dal.models.PersistedUserSession;
import com.mycompany.app.frj.dal.models.keys.UserSessionDataKey;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

/**
 * Data Accessor for User Session data.
 *
 * @author alecva
 */
@RequiredArgsConstructor
public class UserSessionAccessorImpl implements UserSessionAccessor {

    private final UserSessionDdbAccessor sessionDdbAccessor;

    @Override
    public void create(final PersistedUserSession persistedUserSession) {
        sessionDdbAccessor.save(domainTypeToItem(persistedUserSession));
    }

    private UserSessionDdbItem domainTypeToItem(final PersistedUserSession persistedUserSession) {
        UserSessionDdbItem item = new UserSessionDdbItem();
        item.setUserId(persistedUserSession.getUserId());
        item.setSessionId(persistedUserSession.getSessionId());
        item.setExpirationTimestampMs(persistedUserSession.getExpiryTimestampMs());

        return item;
    }

    @Override
    public Optional<PersistedUserSession> load(final UserSessionDataKey key) {
        return sessionDdbAccessor.load(key.getUserId())
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
