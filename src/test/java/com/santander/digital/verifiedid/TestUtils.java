package com.santander.digital.verifiedid;

import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;

public class TestUtils {
    public static void assertThatThrows(TestUtils.CallableWithExceptions callable, Matcher matcher) {
        boolean doThrow = true;

        try {
            callable.call();
            doThrow = false;
        } catch (Throwable var4) {
            MatcherAssert.assertThat(var4, matcher);
        }

        MatcherAssert.assertThat("Call did not thrown", doThrow, Matchers.is(true));
    }

    @FunctionalInterface
    public interface CallableWithExceptions {
        void call() throws Throwable;
    }
}
