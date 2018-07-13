package com.frjgames.testutils.argmatcher;

import lombok.RequiredArgsConstructor;
import org.mockito.ArgumentMatcher;

/**
 * Implementation of Hamcrest's {@link org.hamcrest.Matcher} which uses an internal FunctionalInterface for syntactic sugar.
 *
 * Example usage:
 * <code>
 *     Matcher<MyType> myCustomMatcher = new TestUtilArgMatcher<>(invoked -> invoked.getData().equals(expectedData));
 * </code>
 *
 * @author alecva
 */
@RequiredArgsConstructor
public class TestUtilArgMatcher<T> extends ArgumentMatcher<T> {

    private final TestUtilArgMatcherLambda<T> argMatcher;

    @Override
    public boolean matches(Object argument) {
        T castedArg;
        try {
            castedArg = (T) argument;
        } catch (ClassCastException e) {
            return false;
        }

        return argMatcher.matches(castedArg);
    }
}
