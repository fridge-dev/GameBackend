package com.frjgames.testutils.interfaces;

/**
 * Same as {@link Runnable} but with exception in method signature, to allow any UT/method under test to use this.
 *
 * @author fridge
 */
@FunctionalInterface
public interface RunnableThrows {

    void runThrows() throws Exception;

}
