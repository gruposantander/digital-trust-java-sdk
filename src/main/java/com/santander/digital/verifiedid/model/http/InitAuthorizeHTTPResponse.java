package com.santander.digital.verifiedid.model.http;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class InitAuthorizeHTTPResponse {

    @JsonProperty("expires_in")
    private Long expiresIn;

    @JsonProperty("request_uri")
    private String requestUri;
}
