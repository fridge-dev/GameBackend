package com.frjgames.app.utils;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;
import org.junit.Test;

/**
 * Tests the {@link UniqueIdUtils} class.
 *
 * @author fridge
 */
public class UniqueIdUtilsTest {

    private static final int NUM_STRESS_TESTS = 20;

    private UniqueIdUtils uniqueIdUtils = UniqueIdUtils.getInstance();

    @Test
    public void newUserId() throws Exception {
        Set<String> uniqueIds = new HashSet<>();
        for (int i = 0; i < NUM_STRESS_TESTS; i++) {
            uniqueIds.add(uniqueIdUtils.newUserId());
        }

        assertEquals(NUM_STRESS_TESTS, uniqueIds.size());
    }

}