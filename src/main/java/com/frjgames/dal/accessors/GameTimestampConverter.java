package com.frjgames.dal.accessors;

import java.util.Calendar;
import java.util.Date;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.time.DateUtils;

/**
 * Utility class for converter timestamps used for querying the games table.
 *
 * @author fridge
 */
@UtilityClass
public class GameTimestampConverter {

    /**
     * Truncates the provided unix timestamp (assumed with millisecond precision) to the nearest hour.
     */
    public static long truncateHour(final long millis) {
        return DateUtils.truncate(new Date(millis), Calendar.HOUR).getTime();
    }

}
