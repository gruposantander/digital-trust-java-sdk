package com.santander.digital.verifiedid.impl.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import com.santander.digital.verifiedid.exceptions.DigitalIdGenericException;
import com.santander.digital.verifiedid.model.http.InitAuthorizeHTTPResponse;
import com.santander.digital.verifiedid.model.init.authorize.InitiateAuthorizeResponse;

public class InitAuthorizeHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public InitiateAuthorizeResponse handleInitAuthorize (final String httpResponse,
                                                          final String nonce,
                                                          final String baseURL,
                                                          final String codeVerifier) {
        try {
            final InitAuthorizeHTTPResponse response = objectMapper.readValue(httpResponse, InitAuthorizeHTTPResponse.class);

            final String url = StringUtils.removeEnd(baseURL, "/");
            return InitiateAuthorizeResponse.builder()
                    .expiration(response.getExpiresIn())
                    .nonce(nonce)
                    .requestObjectUri(response.getRequestUri())
                    .redirectionUri(url + "?request_uri=" + response.getRequestUri())
                    .codeVerifier(codeVerifier)
                    .build();
        } catch (JsonProcessingException e) {
            throw new DigitalIdGenericException(e);
        }
    }
}
