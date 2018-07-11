package com.mycompany.app.frj.app.sessions;

import com.mycompany.app.frj.dal.interfaces.UserSessionAccessor;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;

/**
 * Configuration for instantiating Session related classes.
 *
 * @author alecva
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
