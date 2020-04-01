package com.santander.digital.verifiedid.util;

import org.apache.cxf.rs.security.jose.jwk.JsonWebKey;
import org.apache.cxf.rs.security.jose.jwk.JsonWebKeys;
import org.apache.cxf.rs.security.jose.jwk.JwkUtils;
import com.santander.digital.verifiedid.exceptions.DigitalIdConfigurationException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class KeyUtils {


    public static JsonWebKeys readJWKSFromString (final String json) throws DigitalIdConfigurationException{
        return JwkUtils.readJwkSet(json);
    }

    public static JsonWebKey readJWKFromFile (final String path) {
        try(final InputStream inputStream = Files.newInputStream(Paths.get(path))) {
            return JwkUtils.readJwkKey(inputStream);
        } catch (IOException e) {
            throw new DigitalIdConfigurationException(e);
        }
    }
}
