package com.santander.digital.verifiedid.model.http;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class WellKnownHTTPResponse {

    @JsonProperty("authorization_endpoint")
    private String authorizationEndpoint;

    @JsonProperty("issuer")
    private String issuer;

    @JsonProperty("jwks_uri")
    private String jwksEndpoint;

    @JsonProperty("pushed_authorization_request_endpoint")
    private String initAuthorizeEndpoint;

    @JsonProperty("token_endpoint")
    private String tokenEndpoint;
}
