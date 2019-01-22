package com.frjgames.testutils.argmatcher;

/**
 * Interface to be used in conjunction with {@link TestUtilArgMatcher}.
 *
 * This FunctionalInterface provides ability to create custom matching logic from anonymous lambdas.
 *
 * @author fridge
 */
@FunctionalInterface
public interface TestUtilArgMatcherLambda<T> {

    boolean matches(T invokedArg);

}
