package com.santander.digital.verifiedid.impl.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.santander.digital.verifiedid.model.http.WellKnownHTTPResponse;
import lombok.extern.slf4j.Slf4j;
import com.santander.digital.verifiedid.exceptions.DigitalIdGenericException;
import com.santander.digital.verifiedid.model.OPConfiguration;

@Slf4j
public class WellKnownHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public OPConfiguration handleWellKnown (final String response) {
        final WellKnownHTTPResponse wellKnownHTTPResponse;
        try {
            wellKnownHTTPResponse = objectMapper.readValue(response, WellKnownHTTPResponse.class);
        } catch (JsonProcessingException e) {
            throw new DigitalIdGenericException(e);
        }
        return OPConfiguration.builder()
                .initAuthorizeEndpoint(wellKnownHTTPResponse.getInitAuthorizeEndpoint())
                .authorizationEndpoint(wellKnownHTTPResponse.getAuthorizationEndpoint())
                .issuer(wellKnownHTTPResponse.getIssuer())
                .jwksUri(wellKnownHTTPResponse.getJwksEndpoint())
                .tokenEndpoint(wellKnownHTTPResponse.getTokenEndpoint())
                .build();
    }
}
