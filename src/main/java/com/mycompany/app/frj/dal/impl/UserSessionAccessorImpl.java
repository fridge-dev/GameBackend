package com.mycompany.app.frj.dal.impl;

import com.mycompany.app.frj.dal.ddb.accessors.UserSessionDdbAccessor;
import com.mycompany.app.frj.dal.ddb.items.UserSessionDdbItem;
import com.mycompany.app.frj.dal.interfaces.UserSessionAccessor;
import com.mycompany.app.frj.dal.models.UserSession;
import com.mycompany.app.frj.dal.models.keys.UserSessionDataAccessKey;
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
    public void create(final UserSession userSession) {
        sessionDdbAccessor.save(domainTypeToItem(userSession));
    }

    private UserSessionDdbItem domainTypeToItem(final UserSession userSession) {
        UserSessionDdbItem item = new UserSessionDdbItem();
        item.setUserId(userSession.getUserId());
        item.setSessionId(userSession.getSessionId());
        item.setExpirationTimestampMs(userSession.getExpiryTimestamp());

        return item;
    }

    @Override
    public Optional<UserSession> load(final UserSessionDataAccessKey key) {
        return sessionDdbAccessor.load(key.getUserId())
                .map(this::itemToDomainType);
    }

    private UserSession itemToDomainType(final UserSessionDdbItem sessionDdbItem) {
        return UserSession.builder()
                .userId(sessionDdbItem.getUserId())
                .sessionId(sessionDdbItem.getSessionId())
                .expiryTimestamp(sessionDdbItem.getExpirationTimestampMs())
                .build();
    }
}
