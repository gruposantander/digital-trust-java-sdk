package com.santander.digital.verifiedid.impl.handlers;

import org.apache.cxf.rs.security.jose.jwk.JsonWebKeys;
import org.apache.cxf.rs.security.jose.jwk.JwkUtils;

public class JWKSHandler {
    public JsonWebKeys handleJWKS (final String jwksResponse) {
        return JwkUtils.readJwkSet(jwksResponse);
    }
}
