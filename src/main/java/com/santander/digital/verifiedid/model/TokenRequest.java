package com.santander.digital.verifiedid.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TokenRequest {
    private String redirectUri;
    private String authorizationCode;
    private String codeVerifier;
    private String nonce;
}
