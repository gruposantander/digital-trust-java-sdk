package com.santander.digital.verifiedid.model.init.authorize;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class InitiateAuthorizeResponse {
    private final String redirectionUri;
    private final String nonce;
    private final String codeVerifier;
    private final String requestObjectUri;
    private final Long expiration;
}
