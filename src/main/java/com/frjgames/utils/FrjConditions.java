package com.frjgames.utils;

import java.util.function.Supplier;
import lombok.experimental.UtilityClass;

/**
 * This class is analogous to {@link com.google.common.base.Preconditions}, but has our own custom utility methods.
 *
 * @author fridge
 */
@UtilityClass
public class FrjConditions {

    public static <T extends Exception> void checkArg(final boolean condition, final Supplier<T> exceptionSupplier) throws T {
        if (!condition) {
            throw exceptionSupplier.get();
        }
    }

    public static <T extends Exception> void checkNotNull(final Object value, final Supplier<T> exceptionSupplier) throws T {
        checkArg(null != value, exceptionSupplier);
    }

}
