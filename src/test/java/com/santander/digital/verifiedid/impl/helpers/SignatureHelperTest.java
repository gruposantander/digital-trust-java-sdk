package com.santander.digital.verifiedid.impl.helpers;

import org.junit.Before;
import org.junit.Test;

import java.time.Clock;
import java.time.Instant;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static com.santander.digital.verifiedid.TestConstants.*;


public class SignatureHelperTest {

    private SignatureHelper signatureHelper;
    private Clock clock;


    @Before
    public void before() {
        clock = mock(Clock.class);
        when(clock.instant()).thenReturn(Instant.ofEpochSecond(NOW));
        signatureHelper = new SignatureHelper(clock);
    }

    @Test
    public void createJWTRequest () {
        String jwt = signatureHelper.createJWT(
                PRIVATE_JWK,
                "RS256",
                "myself",
                "https://op.iamid.io",
                30L,
                "mySub",
                "my-jti",
                INITIATE_AUTHORIZE_REQUEST_WITH_REDIRECT_URI
        );
        assertThat("Private key modified", PRIVATE_JWK.getAlgorithm(), is(nullValue()));
        assertThat("JWT", jwt, is(SIGNED_JWT_REQUEST));
    }

    @Test
    public void createJWTAuth () {
        String jwt = signatureHelper.createJWT(
                PRIVATE_JWK,
                "RS256",
                "myself",
                "https://op.iamid.io",
                30L,
                "mySub",
                "my-jti",
                INITIATE_AUTHORIZE_AUTH
        );
        assertThat("Private key modified", PRIVATE_JWK.getAlgorithm(), is(nullValue()));
        assertThat("JWT", jwt, is(SIGNED_JWT_AUTH));
    }

    @Test
    public void verifyShouldWork () {
        boolean verification = signatureHelper.verifySignature(JWKS, SIGNED_ID_TOKEN);
        assertThat("wrong verification", verification, is(true));
    }
}