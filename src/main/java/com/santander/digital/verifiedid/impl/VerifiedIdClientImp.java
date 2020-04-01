package com.santander.digital.verifiedid.impl;

import com.santander.digital.verifiedid.http.OpClient;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.cxf.rs.security.jose.jwk.JsonWebKey;
import org.apache.cxf.rs.security.jose.jwk.JsonWebKeys;
import org.apache.cxf.rs.security.jose.jws.JwsCompactConsumer;
import org.json.JSONException;
import org.json.JSONObject;
import com.santander.digital.verifiedid.VerifiedIdClient;
import com.santander.digital.verifiedid.exceptions.DigitalIdGenericException;
import com.santander.digital.verifiedid.exceptions.HTTPClientException;
import com.santander.digital.verifiedid.impl.handlers.InitAuthorizeHandler;
import com.santander.digital.verifiedid.impl.handlers.JWKSHandler;
import com.santander.digital.verifiedid.impl.handlers.TokenHandler;
import com.santander.digital.verifiedid.impl.handlers.WellKnownHandler;
import com.santander.digital.verifiedid.impl.helpers.DeepLinkHelper;
import com.santander.digital.verifiedid.impl.helpers.RandomHelper;
import com.santander.digital.verifiedid.impl.helpers.SignatureHelper;
import com.santander.digital.verifiedid.model.OPConfiguration;
import com.santander.digital.verifiedid.model.TokenRequest;
import com.santander.digital.verifiedid.model.init.authorize.InitiateAuthorizeRequest;
import com.santander.digital.verifiedid.model.init.authorize.InitiateAuthorizeResponse;
import com.santander.digital.verifiedid.model.token.IdToken;

import java.util.Base64;
import java.util.Objects;

import static com.santander.digital.verifiedid.util.CommonUtils.requireNonNull;


public class VerifiedIdClientImp implements VerifiedIdClient {

    private static final JSONObject EMPTY_PAYLOAD = new JSONObject();

    private final String wellKnownURI;
    private final JsonWebKey privateJWK;
    private final String algorithm;
    private final String clientId;

    private final OpClient opClient;

    private final WellKnownHandler wellKnownHandler;
    private final JWKSHandler jwksHandler;
    private final InitAuthorizeHandler initAuthorizeHandler;
    private final TokenHandler tokenHandler;
    private final SignatureHelper signatureHelper;
    private final RandomHelper randomHelper;
    private final DeepLinkHelper deepLinkHelper;


    private OPConfiguration opConfiguration;

    VerifiedIdClientImp(final VerifiedIdClientImpBuilder builder) {
        this.wellKnownURI = requireNonNull(builder.getWellKnownURI(), "Null value of wellKnownURI");
        this.privateJWK = requireNonNull(builder.getPrivateJWK(), "Null value of privateJWK");
        this.algorithm = requireNonNull(builder.getAlgorithm(), "Null value of algorithm");
        this.clientId = requireNonNull(builder.getClientId(), "Null value of clientId");
        this.opClient = requireNonNull(builder.getOpClient(), "Null value of opClient");
        this.wellKnownHandler = requireNonNull(builder.getWellKnownHandler(), "Null value of wellKnownHandler");
        this.initAuthorizeHandler = requireNonNull(builder.getInitAuthorizeHandler(), "Null value of initAuthorizeHandler");
        this.tokenHandler = requireNonNull(builder.getTokenHandler(), "Null value of tokenHandler");
        this.jwksHandler = requireNonNull(builder.getJwksHandler(), "Null value of jwksHandler");
        this.signatureHelper = requireNonNull(builder.getSignatureHelper(), "Null value of signatureHelper");
        this.randomHelper = requireNonNull(builder.getRandomHelper(), "Null value of randomHelper");
        this.deepLinkHelper = requireNonNull(builder.getDeepLinkHelper(), "Null value of deepLinkHelper");
    }


    @Override
    public void setUpClient() {
        try {
            final String wellKnownResponse = opClient.callSimpleGET(this.wellKnownURI);
            this.opConfiguration = this.wellKnownHandler.handleWellKnown(wellKnownResponse);
            final String jwksResponse = this.opClient.callSimpleGET(opConfiguration.getJwksUri());
            final JsonWebKeys jwks = this.jwksHandler.handleJWKS(jwksResponse);
            this.opConfiguration.setJwks(jwks);
        } catch (HTTPClientException e) {
            throw new DigitalIdGenericException(e);
        }
    }

    @Override
    public InitiateAuthorizeResponse initiateAuthorize(final InitiateAuthorizeRequest request) {
        final String authJWT = signatureHelper.createJWT(
                this.privateJWK,
                this.algorithm,
                this.clientId,
                this.opConfiguration.getIssuer(),
                30L,
                this.clientId,
                randomHelper.randomString(),
                EMPTY_PAYLOAD
        );
        if (request.getNonce() == null) {
            request.setNonce("nonce-" + randomHelper.randomString());
        }
        final String codeVerifier;
        if (request.getCodeChallenge() == null && deepLinkHelper.isDeepLink(request.getRedirectUri())) {
            codeVerifier = randomHelper.randomString();
            final byte[] codeChallenge = DigestUtils.sha256(codeVerifier);
            final String codeChallengeAsString = Base64.getUrlEncoder().withoutPadding().encodeToString(codeChallenge);
            request.setCodeChallenge(codeChallengeAsString);
        } else {
            codeVerifier = null;
        }
        final JSONObject jsonRequest = request.toJSON();
        try {
            jsonRequest.put("scope", "openid");
            jsonRequest.put("response_type", "code");
            jsonRequest.put("client_id", this.clientId);
        } catch (JSONException e) {
            throw new DigitalIdGenericException(e);
        }
        final String requestJWT = signatureHelper.createJWT(
                this.privateJWK,
                this.algorithm,
                this.clientId,
                this.opConfiguration.getIssuer(),
                30L,
                this.clientId,
                randomHelper.randomString(),
                jsonRequest
        );
        final String initAuthorizeResponse;
        try {
            initAuthorizeResponse = this.opClient.callInitAuthorize(
                    this.opConfiguration.getInitAuthorizeEndpoint(),
                    requestJWT,
                    authJWT
            );
        } catch (HTTPClientException e) {
            throw new DigitalIdGenericException(e);
        }
        return initAuthorizeHandler.handleInitAuthorize(
                initAuthorizeResponse,
                request.getNonce(),
                this.opConfiguration.getAuthorizationEndpoint(),
                codeVerifier
        );
    }

    @Override
    public IdToken token(TokenRequest request) {
        final String authJWT = signatureHelper.createJWT(
                this.privateJWK,
                this.algorithm,
                this.clientId,
                this.opConfiguration.getIssuer(),
                30L,
                this.clientId,
                randomHelper.randomString(),
                EMPTY_PAYLOAD
        );
        final String tokenResponse;
        try {
            tokenResponse = opClient.callToken(
                    this.opConfiguration.getTokenEndpoint(),
                    request.getAuthorizationCode(),
                    request.getRedirectUri(),
                    authJWT,
                    request.getCodeVerifier()
            );
        } catch (HTTPClientException e) {
            throw new DigitalIdGenericException(e);
        }
        final JwsCompactConsumer idTokenJWT = tokenHandler.handleTokenResponse(tokenResponse);
        final boolean signatureVerification = signatureHelper.verifySignature(this.opConfiguration.getJwks(), idTokenJWT);

        if (!signatureVerification) {
            throw new DigitalIdGenericException("invalid signature");
        }

        final IdToken idToken = tokenHandler.extractTokenData(idTokenJWT);

        final String nonce = request.getNonce();
        if (Objects.nonNull(nonce) && !Objects.equals(idToken.getNonce(), nonce)) {
            throw new DigitalIdGenericException("nonce mismatches");
        }

        return idToken;
    }
}
