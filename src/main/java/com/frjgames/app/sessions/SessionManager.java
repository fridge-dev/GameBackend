package com.frjgames.app.sessions;

import com.frjgames.app.sessions.models.CreateSessionInput;
import com.frjgames.app.sessions.models.SessionData;

/**
 * A session manager is responsible for generating and validating sessions based on a unique
 * session token, which identifies the user and validates they've recently signed in.
 *
 * @author fridge
 */
public interface SessionManager {

    /**
     * Create a new session for the provided user.
     */
    SessionData createSession(CreateSessionInput input);

    /**
     * Determine if the provided session should be considered valid, which will allow the
     * specified user to take actions.
     */
    boolean isValidSession(SessionData sessionData);

}
