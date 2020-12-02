package com.santander.digital.verifiedid.model.claims.verifying;

import org.junit.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class AssertionClaimSimpleTest {

    @Test
    public void prepareOperandShouldTransformDate() {
        final AssertionClaimSimple<Date> assertionClaimSimple = new AssertionClaimSimple<>("birthdate");
        assertThat(
                "wrong string transformation",
                assertionClaimSimple.prepareOperand(Date.from(Instant.EPOCH)),
                is("01/01/1970")
        );

    }

    @Test
    public void prepareOperandShouldNotTransformBigDecimal() {
        final AssertionClaimSimple<BigDecimal> assertionClaimSimple = new AssertionClaimSimple<>("birthdate");
        assertThat(
                "wrong string transformation",
                assertionClaimSimple.prepareOperand(BigDecimal.ONE),
                is(BigDecimal.valueOf(1))
        );

    }
}