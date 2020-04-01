package com.santander.digital.verifiedid.model;

import lombok.Builder;
import lombok.Data;
import org.apache.cxf.rs.security.jose.jwk.JsonWebKeys;

@Builder
@Data
public class OPConfiguration {
    private final String initAuthorizeEndpoint;
    private final String authorizationEndpoint;
    private final String tokenEndpoint;
    private final String issuer;
    private final String jwksUri;
    private JsonWebKeys jwks;
}
