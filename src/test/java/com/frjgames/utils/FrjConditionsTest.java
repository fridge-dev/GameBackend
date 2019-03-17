package com.frjgames.utils;

import com.frjgames.testutils.TestUtilExceptionValidator;
import java.io.IOException;
import java.util.function.Supplier;
import org.junit.Test;

/**
 * Tests the {@link FrjConditions} class.
 *
 * @author fridge
 */
public class FrjConditionsTest {

    private static final Supplier<IOException> CHECKED_EXCEPTION = () -> new IOException("fake");
    private static final Supplier<IllegalStateException> UNCHECKED_EXCEPTION = () -> new IllegalStateException("fake");

    @Test
    public void checkArg() throws Exception {
        FrjConditions.checkArg(true, CHECKED_EXCEPTION);
        FrjConditions.checkArg(true, UNCHECKED_EXCEPTION);

        TestUtilExceptionValidator.assertThrows(IOException.class,
                () -> FrjConditions.checkArg(false, CHECKED_EXCEPTION)
        );
        TestUtilExceptionValidator.assertThrows(IllegalStateException.class,
                () -> FrjConditions.checkArg(false, UNCHECKED_EXCEPTION)
        );
    }

    @Test
    public void checkNotNull() throws Exception {
        FrjConditions.checkNotNull("", CHECKED_EXCEPTION);
        FrjConditions.checkNotNull("", UNCHECKED_EXCEPTION);

        TestUtilExceptionValidator.assertThrows(IOException.class,
                () -> FrjConditions.checkNotNull(null, CHECKED_EXCEPTION)
        );
        TestUtilExceptionValidator.assertThrows(IllegalStateException.class,
                () -> FrjConditions.checkNotNull(null, UNCHECKED_EXCEPTION)
        );
    }

}