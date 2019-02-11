package com.frjgames.utils;

import static org.junit.Assert.assertEquals;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests the {@link TimeUtils} class.
 *
 * @author fridge
 */
@RunWith(DataProviderRunner.class)
public class TimeUtilsTest {

    @DataProvider
    public static Object[][] dataProvider() {
        return new Object[][] {
                { 1548314327123L, 1548313200000L },
                { 1548313200000L, 1548313200000L },
                { 1548313199999L, 1548309600000L },
        };
    }

    @Test
    @UseDataProvider("dataProvider")
    public void test(final long timestampMs, final long expected) throws Exception {
        assertEquals(expected, TimeUtils.truncateHour(timestampMs));
    }
}