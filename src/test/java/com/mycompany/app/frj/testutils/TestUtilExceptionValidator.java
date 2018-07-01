package com.mycompany.app.frj.testutils;

import static org.junit.Assert.fail;

import com.mycompany.app.frj.testutils.interfaces.RunnableThrows;

/**
 * A test utility for all validations related to exceptions.
 *
 * @author alecva
 */
public class TestUtilExceptionValidator {

    /**
     * Validate an exception is thrown by the method under test.
     */
    public static <T extends Throwable> void validateThrown(final Class<T> clazz, final RunnableThrows runnable) throws Exception {
        try {
            runnable.runThrows();
            fail(String.format("Expected exception of type %s", clazz.getName()));
        } catch (Exception e) {
            if (!clazz.equals(e.getClass())) {
                throw e;
            }
        }
    }

    /**
     * Validate IllegalArgumentException is thrown by method under test.
     *
     * This is a separate util, since it is extremely common in our UTs to validate for IAE.
     * Not every exception should get its own method, like this.
     */
    public static void validateIllegalArg(final RunnableThrows runnable) throws Exception {
        validateThrown(IllegalArgumentException.class, runnable);
    }

}
