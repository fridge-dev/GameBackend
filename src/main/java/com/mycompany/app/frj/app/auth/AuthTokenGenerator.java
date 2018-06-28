package com.mycompany.app.frj.app.auth;

import com.mycompany.app.frj.dal.models.User;
import java.util.Base64;

/**
 * TODO
 *
 * @author alecva
 */
public class AuthTokenGenerator {

    /**
     * TODO
     *
     * - Move b64 to util
     */
    public String generateToken(final User user) {
        byte[] userId = user.getUserId().getBytes();

        byte[] encodedId = Base64.getEncoder().encode(userId);

        return new String(encodedId);
    }
}
