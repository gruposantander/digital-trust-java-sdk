package com.santander.digital.verifiedid.impl.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.cxf.rs.security.jose.jws.JwsCompactConsumer;
import com.santander.digital.verifiedid.exceptions.DigitalIdGenericException;
import com.santander.digital.verifiedid.model.http.TokenHTTPResponse;
import com.santander.digital.verifiedid.model.token.IdToken;

public class TokenHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public JwsCompactConsumer handleTokenResponse (final String httpResponse) {

        final TokenHTTPResponse response;
        try {
            response = objectMapper.readValue(httpResponse, TokenHTTPResponse.class);
        } catch (JsonProcessingException e) {
            throw new DigitalIdGenericException(e);
        }
        return new JwsCompactConsumer(response.getIdToken());
    }

    public IdToken extractTokenData (final JwsCompactConsumer jwt) {
        final String payload = jwt.getDecodedJwsPayload();
        try {
            return objectMapper.readValue(payload, IdToken.class);
        } catch (JsonProcessingException e) {
            throw new DigitalIdGenericException(e);
        }
    }
}
