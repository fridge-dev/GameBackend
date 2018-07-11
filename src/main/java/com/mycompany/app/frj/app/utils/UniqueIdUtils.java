package com.mycompany.app.frj.app.utils;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Utility class for creating unique IDs.
 *
 * @author alecva
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UniqueIdUtils {

    @Getter
    private static UniqueIdUtils instance = new UniqueIdUtils();

    /**
     * @return a new random user ID.
     */
    public String newUserId() {
        return UUID.randomUUID().toString();
    }

}
