package com.frjgames.app.internal.sessions;

import com.frjgames.dal.models.interfaces.UserSessionAccessor;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;

/**
 * Configuration for instantiating Session related classes.
 *
 * @author fridge
 */
@RequiredArgsConstructor
public class SessionModule {

    /**
     * Dependencies
     */
    private final UserSessionAccessor userSessionAccessor;

    /**
     * Local singletons
     */
    private SessionManager sessionManager;

    @Synchronized
    public SessionManager sessionManager() {
        if (sessionManager == null) {
            sessionManager = new RngSessionManager(userSessionAccessor);
        }

        return sessionManager;
    }

}
