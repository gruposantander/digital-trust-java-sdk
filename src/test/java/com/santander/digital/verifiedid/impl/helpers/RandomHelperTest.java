package com.santander.digital.verifiedid.impl.helpers;


import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class RandomHelperTest {

    private RandomHelper randomHelper = new RandomHelper();

    @Test
    public void testRandomStringShouldReturnAUUID () {
        final String randomString = randomHelper.randomString();
        assertThat("wrong length", randomString.length(), is(43));
    }

}