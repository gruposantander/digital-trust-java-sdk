package com.santander.digital.verifiedid.util;

import com.santander.digital.verifiedid.TestConstants;
import com.santander.digital.verifiedid.TestUtils;
import com.santander.digital.verifiedid.exceptions.DigitalIdConfigurationException;
import org.apache.cxf.rs.security.jose.jwk.JsonWebKey;
import org.apache.cxf.rs.security.jose.jwk.JsonWebKeys;
import org.apache.cxf.rs.security.jose.jwk.KeyType;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;

public class KeyUtilsTest {

    @Test
    public void readJWKSFromStringShouldWorkForWellFormedJWKS () {
        JsonWebKeys jsonWebKeys = KeyUtils.readJWKSFromString(TestConstants.JWKS_STRING);
        assertThat("wrong number of keys", jsonWebKeys.getKeys().size(), is(1));
    }

    @Test
    public void readJWKShouldSucceed () {
        JsonWebKey jsonWebKey = KeyUtils.readJWKFromFile("./src/test/resources/private.json");
        assertThat("wrong kid", jsonWebKey.getKeyId(), is("test-key"));
        assertThat("wrong key type", jsonWebKey.getKeyType(), is(KeyType.RSA));
    }

    @Test
    public void readJWKShouldFailIfKeyDoesNotxist () {
        TestUtils.assertThatThrows(() -> KeyUtils.readJWKFromFile("./src/test/resources/does.not.exist"),
                CoreMatchers.allOf(
                        isA(DigitalIdConfigurationException.class),
                        hasProperty("message", is("java.nio.file.NoSuchFileException: ./src/test/resources/does.not.exist"))
                )
        );
    }


}