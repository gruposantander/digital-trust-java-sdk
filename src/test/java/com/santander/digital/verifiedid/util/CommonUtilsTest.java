package com.santander.digital.verifiedid.util;


import org.junit.Test;

import java.sql.Date;
import java.time.Instant;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CommonUtilsTest {

    @Test
    public void isoDateShouldParseCorrectly () {
        String iso = CommonUtils.isoDate(Date.from(Instant.ofEpochSecond(1581961971)));
        assertThat("wrong parsing", iso, is("2020-02-17"));
    }
}