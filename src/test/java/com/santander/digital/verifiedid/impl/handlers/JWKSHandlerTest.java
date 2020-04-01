package com.santander.digital.verifiedid.impl.handlers;

import org.apache.cxf.rs.security.jose.jwk.JsonWebKeys;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static com.santander.digital.verifiedid.TestConstants.JWKS;
import static com.santander.digital.verifiedid.TestConstants.JWKS_STRING;

public class JWKSHandlerTest {

    private JWKSHandler jwksHandler;

    @Before
    public void before () {
        jwksHandler = new JWKSHandler();
    }

    @Test
    public void handleJWKSShouldSucceed () {
        JsonWebKeys jsonWebKeys = jwksHandler.handleJWKS(JWKS_STRING);

        assertThat("wrong jwks extracted", jsonWebKeys.getKey("op_key_1"), is(JWKS.getKey("op_key_1")));
    }

    //TODO negative scenarios
}