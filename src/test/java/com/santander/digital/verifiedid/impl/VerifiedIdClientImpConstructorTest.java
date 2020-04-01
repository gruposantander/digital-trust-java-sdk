package com.santander.digital.verifiedid.impl;

import com.santander.digital.verifiedid.TestConstants;
import com.santander.digital.verifiedid.TestUtils;
import com.santander.digital.verifiedid.exceptions.DigitalIdConfigurationException;
import com.santander.digital.verifiedid.util.KeyUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.cxf.rs.security.jose.jwk.JsonWebKey;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;

public class VerifiedIdClientImpConstructorTest {

    @Test
    public void buildShouldSucceedIfPrivateKeyIsPassedDirectly () throws Exception {
        JsonWebKey jwk = KeyUtils.readJWKFromFile("./src/test/resources/private.json");
        VerifiedIdClientImp client = new VerifiedIdClientImpBuilder()
                .withClientId(TestConstants.CLIENT_ID)
                .withWellKnownURI(TestConstants.WELL_KNOWN_COMPLETE_URI)
                .withPrivateJWK(jwk)
                .build();
        assertThat("Wrong algorithm", FieldUtils.readField(client, "privateJWK", true), is(jwk));
    }

    @Test
    public void buildShouldSucceedIfAlgorithmIsNotPassedAlgorithmSetToDefault () throws Exception {
        VerifiedIdClientImp client = new VerifiedIdClientImpBuilder()
                .withClientId(TestConstants.CLIENT_ID)
                .withWellKnownURI(TestConstants.WELL_KNOWN_COMPLETE_URI)
                .withPrivateJWKFromFile("./src/test/resources/private.json")
                .build();
        assertThat("Wrong algorithm", FieldUtils.readField(client, "algorithm", true), is("RS256"));
    }

    @Test
    public void buildShouldSucceedIfAndAlgorithmSetToCustom () throws Exception {
        VerifiedIdClientImp client = new VerifiedIdClientImpBuilder()
                .withClientId(TestConstants.CLIENT_ID)
                .withWellKnownURI(TestConstants.WELL_KNOWN_COMPLETE_URI)
                .withAlgorithm("MY_ALG")
                .withPrivateJWKFromFile("./src/test/resources/private.json")
                .build();
        assertThat("Wrong algorithm", FieldUtils.readField(client, "algorithm", true), is("MY_ALG"));
    }

    @Test
    public void buildShouldFailIfClientIdIsMissing () throws Exception {
        TestUtils.assertThatThrows(() ->
                new VerifiedIdClientImpBuilder()
                        .withWellKnownURI(TestConstants.WELL_KNOWN_COMPLETE_URI)
                        .withPrivateJWKFromFile("./src/test/resources/private.json")
                        .build(), CoreMatchers.allOf(isA(DigitalIdConfigurationException.class), hasProperty("message", is("Null value of clientId")))
        );
    }

    @Test
    public void buildShouldFailIfWellKnownUriIsMissing () throws Exception {
        TestUtils.assertThatThrows(() ->
                new VerifiedIdClientImpBuilder()
                        .withClientId(TestConstants.CLIENT_ID)
                        .withPrivateJWKFromFile("./src/test/resources/private.json")
                        .build(), allOf(isA(DigitalIdConfigurationException.class), hasProperty("message", is("Null value of wellKnownURI")))
        );
    }

    @Test
    public void buildShouldFailIfPrivateKeyIsMissing () throws Exception {
        TestUtils.assertThatThrows(() ->
                new VerifiedIdClientImpBuilder()
                        .withClientId(TestConstants.CLIENT_ID)
                        .withWellKnownURI(TestConstants.WELL_KNOWN_COMPLETE_URI)
                        .build(), allOf(isA(DigitalIdConfigurationException.class), hasProperty("message", is("Null value of privateJWK")))
        );
    }

}