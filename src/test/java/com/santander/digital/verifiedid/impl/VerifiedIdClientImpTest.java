package com.santander.digital.verifiedid.impl;

import com.santander.digital.verifiedid.TestConstants;
import com.santander.digital.verifiedid.helpers.VerifiedIdClientImpBuilderHelper;
import com.santander.digital.verifiedid.http.OpClient;
import com.santander.digital.verifiedid.impl.handlers.InitAuthorizeHandler;
import com.santander.digital.verifiedid.impl.handlers.JWKSHandler;
import com.santander.digital.verifiedid.impl.handlers.WellKnownHandler;
import com.santander.digital.verifiedid.impl.helpers.SignatureHelper;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.hamcrest.CoreMatchers;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import com.santander.digital.verifiedid.exceptions.DigitalIdGenericException;
import com.santander.digital.verifiedid.exceptions.HTTPClientException;
import com.santander.digital.verifiedid.impl.handlers.TokenHandler;
import com.santander.digital.verifiedid.impl.helpers.RandomHelper;
import com.santander.digital.verifiedid.model.OPConfiguration;
import com.santander.digital.verifiedid.model.TokenRequest;
import com.santander.digital.verifiedid.model.init.authorize.InitiateAuthorizeRequest;
import com.santander.digital.verifiedid.model.init.authorize.InitiateAuthorizeResponse;
import com.santander.digital.verifiedid.model.token.IdToken;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static com.santander.digital.verifiedid.TestUtils.assertThatThrows;

public class VerifiedIdClientImpTest {

    public static final JSONObject EMPTY_JSON = new JSONObject();
    private WellKnownHandler wellKnownHandler = mock(WellKnownHandler.class);
    private JWKSHandler jwksHandler = mock(JWKSHandler.class);
    private InitAuthorizeHandler initAuthorizeHandler = mock(InitAuthorizeHandler.class);
    private TokenHandler tokenHandler = mock(TokenHandler.class);
    private OpClient opHTTPClient = mock(OpClient.class);
    private SignatureHelper signatureHelper = mock(SignatureHelper.class);
    private RandomHelper randomHelper = mock(RandomHelper.class);

    private VerifiedIdClientImp verifiedIdClientImp;


    @Before
    public void before() {
        verifiedIdClientImp = new VerifiedIdClientImpBuilderHelper()
                .withWellKnownHandler(wellKnownHandler)
                .withJWKSHandler(jwksHandler)
                .withInitAuthorizeHandler(initAuthorizeHandler)
                .withTokenHandler(tokenHandler)
                .withSignatureHelper(signatureHelper)
                .withUUIDHelper(randomHelper)
                .withOpClient(opHTTPClient)
                .withClientId(TestConstants.CLIENT_ID)
                .withWellKnownURI(TestConstants.WELL_KNOWN_COMPLETE_URI)
                .withPrivateJWKFromFile("./src/test/resources/private.json")
                .build();
    }

    @Test
    public void setUpClientShouldFailsIfHTTPClientFailsFetchingWellKnown() throws Exception {
        given(opHTTPClient.callSimpleGET(anyString()))
                .willThrow(new HTTPClientException("generic error"));

        assertThatThrows(
                () -> verifiedIdClientImp.setUpClient(),
                allOf(
                        isA(DigitalIdGenericException.class),
                        hasProperty("message", is("com.santander.digital.verifiedid.exceptions.HTTPClientException: generic error"))
                )
        );

        then(opHTTPClient).should(times(1)).callSimpleGET(TestConstants.WELL_KNOWN_COMPLETE_URI);
        then(wellKnownHandler).should(never()).handleWellKnown(anyString());
        then(opHTTPClient).should(never()).callSimpleGET(TestConstants.JWKS_COMPLETE_URI);
        then(jwksHandler).should(never()).handleJWKS(anyString());
    }

    @Test
    public void setUpClientShouldFailsIfHTTPClientFailsFetchingJWKS() throws Exception {
        given(opHTTPClient.callSimpleGET(anyString()))
                .willReturn(TestConstants.WELL_KNOWN_STRING)
                .willThrow(new HTTPClientException("generic error"));
        given(wellKnownHandler.handleWellKnown(any()))
                .willReturn(TestConstants.OP_CONFIGURATION);

        assertThatThrows(
                () -> verifiedIdClientImp.setUpClient(),
                allOf(
                        isA(DigitalIdGenericException.class),
                        hasProperty("message", is("com.santander.digital.verifiedid.exceptions.HTTPClientException: generic error"))
                )
        );

        then(opHTTPClient).should(times(1)).callSimpleGET(TestConstants.WELL_KNOWN_COMPLETE_URI);
        then(wellKnownHandler).should().handleWellKnown(anyString());
        then(opHTTPClient).should().callSimpleGET(TestConstants.JWKS_COMPLETE_URI);
        then(jwksHandler).should(never()).handleJWKS(anyString());
    }

    @Test
    public void setUpClientShouldSucceed() throws Exception {
        given(opHTTPClient.callSimpleGET(anyString()))
                .willReturn(TestConstants.WELL_KNOWN_STRING)
                .willReturn(TestConstants.JWKS_STRING);
        given(wellKnownHandler.handleWellKnown(any()))
                .willReturn(TestConstants.OP_CONFIGURATION);
        given(jwksHandler.handleJWKS(any()))
                .willReturn(TestConstants.JWKS);

        verifiedIdClientImp.setUpClient();

        OPConfiguration opConfiguration = (OPConfiguration) FieldUtils.readField(verifiedIdClientImp, "opConfiguration", true);

        assertThat("Wrong init-authorize endpoint", opConfiguration.getInitAuthorizeEndpoint(), CoreMatchers.is(TestConstants.INIT_AUTHORIZE_COMPLETE_URI));
        assertThat("Wrong login endpoint", opConfiguration.getAuthorizationEndpoint(), CoreMatchers.is(TestConstants.AUTHORIZE_COMPLETE_URI));
        assertThat("Wrong token endpoint", opConfiguration.getTokenEndpoint(), CoreMatchers.is(TestConstants.TOKEN_COMPLETE_URI));
        assertThat("Wrong issuer", opConfiguration.getIssuer(), CoreMatchers.is(TestConstants.ISSUER));
        assertThat("Wrong jwk uri", opConfiguration.getJwksUri(), CoreMatchers.is(TestConstants.JWKS_COMPLETE_URI));
        assertThat("Wrong jwk provider", opConfiguration.getJwks(), CoreMatchers.is(TestConstants.JWKS));

        then(opHTTPClient).should(times(1)).callSimpleGET(TestConstants.WELL_KNOWN_COMPLETE_URI);
        then(wellKnownHandler).should().handleWellKnown(TestConstants.WELL_KNOWN_STRING);
        then(opHTTPClient).should(times(1)).callSimpleGET(TestConstants.JWKS_COMPLETE_URI);
        then(jwksHandler).should().handleJWKS(TestConstants.JWKS_STRING);
    }

    @Test
    public void initAuthorizeShouldSucceedWithRedirectUri() throws Exception {
        given(randomHelper.randomString())
                .willReturn(TestConstants.TEST_UUID);
        given(signatureHelper.createJWT(
                any(),
                anyString(),
                anyString(),
                anyString(),
                anyLong(),
                anyString(),
                anyString(),
                any()))
                .willReturn(TestConstants.SIGNED_JWT_AUTH)
                .willReturn(TestConstants.SIGNED_JWT_REQUEST);
        given(opHTTPClient.callInitAuthorize(anyString(), any(), any()))
                .willReturn(TestConstants.INITIATE_AUTHORIZE_HTTP_RESPONSE);
        given(initAuthorizeHandler.handleInitAuthorize(anyString(), anyString(), anyString(), any()))
                .willReturn(TestConstants.INITIATE_AUTHORIZE_RESPONSE);

        FieldUtils.writeDeclaredField(verifiedIdClientImp, "opConfiguration", TestConstants.OP_CONFIGURATION, true);

        InitiateAuthorizeRequest request = InitiateAuthorizeRequest.builder()
                .redirectUri("https://rp-example.com/callback")
                .claims(TestConstants.ID_CLAIMS)
                .assertionClaims(TestConstants.ASSERTION_CLAIMS)
                .state("state-123")
                .build();
        InitiateAuthorizeResponse initiateAuthorizeResponse = verifiedIdClientImp.initiateAuthorize(request);

        assertThat("wrong expiration", initiateAuthorizeResponse.getExpiration(), CoreMatchers.is(TestConstants.NOW + 30));
        assertThat("wrong nonce", initiateAuthorizeResponse.getNonce(), CoreMatchers.is(TestConstants.NONCE));
        assertThat("wrong redirection uri", initiateAuthorizeResponse.getRedirectionUri(), CoreMatchers.is(TestConstants.REDIRECTION_URI));
        assertThat("wrong request_uri", initiateAuthorizeResponse.getRequestObjectUri(), CoreMatchers.is(TestConstants.URN));
        assertThat("wrong code verifier", initiateAuthorizeResponse.getCodeVerifier(), is(nullValue()));

        then(randomHelper).should(times(3)).randomString();
        ArgumentCaptor<JSONObject> stringArgumentCaptor = ArgumentCaptor.forClass(JSONObject.class);
        then(signatureHelper).should(times(2)).createJWT(
                eq(TestConstants.PRIVATE_JWK),
                eq("RS256"),
                eq(TestConstants.CLIENT_ID),
                eq(TestConstants.ISSUER),
                eq(30L),
                eq(TestConstants.CLIENT_ID),
                eq(TestConstants.TEST_UUID),
                stringArgumentCaptor.capture()
        );
        assertThat("wrong auth payload", stringArgumentCaptor.getAllValues().get(0).toString(), is(EMPTY_JSON.toString()));
        assertThat("wrong request payload", stringArgumentCaptor.getAllValues().get(1).toString(), CoreMatchers.is(TestConstants.INITIATE_AUTHORIZE_REQUEST_WITH_REDIRECT_URI.toString()));
        then(opHTTPClient).should().callInitAuthorize(eq(TestConstants.INIT_AUTHORIZE_COMPLETE_URI), any(), any());
        then(initAuthorizeHandler).should().handleInitAuthorize(TestConstants.INITIATE_AUTHORIZE_HTTP_RESPONSE, TestConstants.NONCE, TestConstants.AUTHORIZE_COMPLETE_URI, null);
    }

    @Test
    public void initAuthorizeShouldSucceedWithRedirectUriAsDeepLink() throws Exception {
        given(randomHelper.randomString())
                .willReturn(TestConstants.TEST_UUID)
                .willReturn(TestConstants.TEST_UUID)
                .willReturn(TestConstants.TEST_CODE_VERIFIER)
                .willReturn(TestConstants.TEST_UUID);
        given(signatureHelper.createJWT(
                any(),
                anyString(),
                anyString(),
                anyString(),
                anyLong(),
                anyString(),
                anyString(),
                any()))
                .willReturn(TestConstants.SIGNED_JWT_AUTH)
                .willReturn(TestConstants.SIGNED_JWT_REQUEST);
        given(opHTTPClient.callInitAuthorize(anyString(), any(), any()))
                .willReturn(TestConstants.INITIATE_AUTHORIZE_HTTP_RESPONSE);
        given(initAuthorizeHandler.handleInitAuthorize(anyString(), anyString(), anyString(), any()))
                .willReturn(TestConstants.INITIATE_AUTHORIZE_RESPONSE_WITH_CODE_VERIFIER);

        FieldUtils.writeDeclaredField(verifiedIdClientImp, "opConfiguration", TestConstants.OP_CONFIGURATION, true);

        InitiateAuthorizeRequest request = InitiateAuthorizeRequest.builder()
                .redirectUri("rp-example://callback")
                .claims(TestConstants.ID_CLAIMS)
                .assertionClaims(TestConstants.ASSERTION_CLAIMS)
                .state("state-123")
                .build();
        InitiateAuthorizeResponse initiateAuthorizeResponse = verifiedIdClientImp.initiateAuthorize(request);

        assertThat("wrong expiration", initiateAuthorizeResponse.getExpiration(), CoreMatchers.is(TestConstants.NOW + 30));
        assertThat("wrong nonce", initiateAuthorizeResponse.getNonce(), CoreMatchers.is(TestConstants.NONCE));
        assertThat("wrong redirection uri", initiateAuthorizeResponse.getRedirectionUri(), CoreMatchers.is(TestConstants.REDIRECTION_URI));
        assertThat("wrong request_uri", initiateAuthorizeResponse.getRequestObjectUri(), CoreMatchers.is(TestConstants.URN));
        assertThat("wrong code verifier", initiateAuthorizeResponse.getCodeVerifier(), CoreMatchers.is(TestConstants.TEST_CODE_VERIFIER));

        then(randomHelper).should(times(4)).randomString();
        ArgumentCaptor<JSONObject> stringArgumentCaptor = ArgumentCaptor.forClass(JSONObject.class);
        then(signatureHelper).should(times(2)).createJWT(
                eq(TestConstants.PRIVATE_JWK),
                eq("RS256"),
                eq(TestConstants.CLIENT_ID),
                eq(TestConstants.ISSUER),
                eq(30L),
                eq(TestConstants.CLIENT_ID),
                eq(TestConstants.TEST_UUID),
                stringArgumentCaptor.capture()
        );
        assertThat("wrong auth payload", stringArgumentCaptor.getAllValues().get(0).toString(), is(EMPTY_JSON.toString()));
        assertThat("wrong request payload", stringArgumentCaptor.getAllValues().get(1).toString(), CoreMatchers.is(TestConstants.INITIATE_AUTHORIZE_REQUEST_WITH_REDIRECT_URI_AND_CODE_CHALLENGE.toString()));
        then(opHTTPClient).should().callInitAuthorize(eq(TestConstants.INIT_AUTHORIZE_COMPLETE_URI), any(), any());
        then(initAuthorizeHandler).should().handleInitAuthorize(TestConstants.INITIATE_AUTHORIZE_HTTP_RESPONSE, TestConstants.NONCE, TestConstants.AUTHORIZE_COMPLETE_URI, TestConstants.TEST_CODE_VERIFIER);
    }

    @Test
    public void initAuthorizeShouldSucceedWithoutRedirectUri() throws Exception {
        given(randomHelper.randomString())
                .willReturn(TestConstants.TEST_UUID);
        given(signatureHelper.createJWT(
                any(),
                anyString(),
                anyString(),
                anyString(),
                anyLong(),
                anyString(),
                anyString(),
                any()))
                .willReturn(TestConstants.SIGNED_JWT_AUTH)
                .willReturn(TestConstants.SIGNED_JWT_REQUEST);
        given(opHTTPClient.callInitAuthorize(anyString(), any(), any()))
                .willReturn(TestConstants.INITIATE_AUTHORIZE_HTTP_RESPONSE);
        given(initAuthorizeHandler.handleInitAuthorize(anyString(), anyString(), anyString(), any()))
                .willReturn(TestConstants.INITIATE_AUTHORIZE_RESPONSE);

        FieldUtils.writeDeclaredField(verifiedIdClientImp, "opConfiguration", TestConstants.OP_CONFIGURATION, true);

        InitiateAuthorizeRequest request = InitiateAuthorizeRequest.builder()
                .claims(TestConstants.ID_CLAIMS)
                .assertionClaims(TestConstants.ASSERTION_CLAIMS)
                .state("state-123")
                .build();
        InitiateAuthorizeResponse initiateAuthorizeResponse = verifiedIdClientImp.initiateAuthorize(request);

        assertThat("wrong expiration", initiateAuthorizeResponse.getExpiration(), CoreMatchers.is(TestConstants.NOW + 30));
        assertThat("wrong nonce", initiateAuthorizeResponse.getNonce(), CoreMatchers.is(TestConstants.NONCE));
        assertThat("wrong redirection uri", initiateAuthorizeResponse.getRedirectionUri(), CoreMatchers.is(TestConstants.REDIRECTION_URI));
        assertThat("wrong request_uri", initiateAuthorizeResponse.getRequestObjectUri(), CoreMatchers.is(TestConstants.URN));
        assertThat("wrong code verifier", initiateAuthorizeResponse.getCodeVerifier(), is(nullValue()));

        then(randomHelper).should(times(3)).randomString();
        ArgumentCaptor<JSONObject> stringArgumentCaptor = ArgumentCaptor.forClass(JSONObject.class);
        then(signatureHelper).should(times(2)).createJWT(
                eq(TestConstants.PRIVATE_JWK),
                eq("RS256"),
                eq(TestConstants.CLIENT_ID),
                eq(TestConstants.ISSUER),
                eq(30L),
                eq(TestConstants.CLIENT_ID),
                eq(TestConstants.TEST_UUID),
                stringArgumentCaptor.capture()
        );
        assertThat("wrong auth payload", stringArgumentCaptor.getAllValues().get(0).toString(), is(EMPTY_JSON.toString()));
        assertThat("wrong request payload", stringArgumentCaptor.getAllValues().get(1).toString(), CoreMatchers.is(TestConstants.INITIATE_AUTHORIZE_REQUEST_WITHOUT_REDIRECT_URI.toString()));
        then(opHTTPClient).should().callInitAuthorize(eq(TestConstants.INIT_AUTHORIZE_COMPLETE_URI), any(), any());
        then(initAuthorizeHandler).should().handleInitAuthorize(TestConstants.INITIATE_AUTHORIZE_HTTP_RESPONSE, TestConstants.NONCE, TestConstants.AUTHORIZE_COMPLETE_URI, null);
    }

    @Test
    public void tokenShouldSucceedWithoutRedirectURI() throws Exception {
        given(randomHelper.randomString())
                .willReturn(TestConstants.TEST_UUID);
        given(signatureHelper.createJWT(
                any(),
                anyString(),
                anyString(),
                anyString(),
                anyLong(),
                anyString(),
                anyString(),
                any()))
                .willReturn(TestConstants.SIGNED_JWT_AUTH);
        given(opHTTPClient.callToken(anyString(), anyString(), any(), anyString(), any()))
                .willReturn(TestConstants.TOKEN_HTTP_RESPONSE);
        given(tokenHandler.handleTokenResponse(anyString()))
                .willReturn(TestConstants.SIGNED_ID_TOKEN);
        given(signatureHelper.verifySignature(any(), any()))
                .willReturn(true);
        given(tokenHandler.extractTokenData(any()))
                .willReturn(TestConstants.ID_TOKEN_WITH_NONCE);

        FieldUtils.writeDeclaredField(verifiedIdClientImp, "opConfiguration", TestConstants.OP_CONFIGURATION_WITH_JWKS, true);

        TokenRequest tokenRequest = TokenRequest.builder()
                .authorizationCode("MY_CODE")
                .build();

        IdToken idToken = verifiedIdClientImp.token(tokenRequest);

        assertThat("Wrong token", idToken, is(TestConstants.ID_TOKEN_WITH_NONCE));

        then(randomHelper).should().randomString();
        ArgumentCaptor<JSONObject> stringArgumentCaptor = ArgumentCaptor.forClass(JSONObject.class);
        then(signatureHelper).should().createJWT(
                eq(TestConstants.PRIVATE_JWK),
                eq("RS256"),
                eq(TestConstants.CLIENT_ID),
                eq(TestConstants.ISSUER),
                eq(30L),
                eq(TestConstants.CLIENT_ID),
                eq(TestConstants.TEST_UUID),
                stringArgumentCaptor.capture()
        );
        assertThat("wrong auth payload", stringArgumentCaptor.getAllValues().get(0).toString(), is(EMPTY_JSON.toString()));
        then(opHTTPClient).should().callToken(
                TestConstants.TOKEN_COMPLETE_URI,
                "MY_CODE",
                null,
                TestConstants.SIGNED_JWT_AUTH,
                null
        );
        then(tokenHandler).should().handleTokenResponse(TestConstants.TOKEN_HTTP_RESPONSE);
        then(signatureHelper).should().verifySignature(TestConstants.JWKS, TestConstants.SIGNED_ID_TOKEN);
    }

    @Test
    public void tokenShouldSucceedWithoutNonce() throws Exception {
        given(randomHelper.randomString())
                .willReturn(TestConstants.TEST_UUID);
        given(signatureHelper.createJWT(
                any(),
                anyString(),
                anyString(),
                anyString(),
                anyLong(),
                anyString(),
                anyString(),
                any()))
                .willReturn(TestConstants.SIGNED_JWT_AUTH);
        given(opHTTPClient.callToken(anyString(), anyString(), anyString(), anyString(), any()))
                .willReturn(TestConstants.TOKEN_HTTP_RESPONSE);
        given(tokenHandler.handleTokenResponse(anyString()))
                .willReturn(TestConstants.SIGNED_ID_TOKEN);
        given(signatureHelper.verifySignature(any(), any()))
                .willReturn(true);
        given(tokenHandler.extractTokenData(any()))
                .willReturn(TestConstants.ID_TOKEN_WITH_NONCE);

        FieldUtils.writeDeclaredField(verifiedIdClientImp, "opConfiguration", TestConstants.OP_CONFIGURATION_WITH_JWKS, true);

        TokenRequest tokenRequest = TokenRequest.builder()
                .redirectUri("https://rp-example.com/callback")
                .authorizationCode("MY_CODE")
                .build();

        IdToken idToken = verifiedIdClientImp.token(tokenRequest);

        assertThat("Wrong token", idToken, is(TestConstants.ID_TOKEN_WITH_NONCE));

        then(randomHelper).should().randomString();
        ArgumentCaptor<JSONObject> stringArgumentCaptor = ArgumentCaptor.forClass(JSONObject.class);
        then(signatureHelper).should().createJWT(
                eq(TestConstants.PRIVATE_JWK),
                eq("RS256"),
                eq(TestConstants.CLIENT_ID),
                eq(TestConstants.ISSUER),
                eq(30L),
                eq(TestConstants.CLIENT_ID),
                eq(TestConstants.TEST_UUID),
                stringArgumentCaptor.capture()
        );
        assertThat("wrong auth payload", stringArgumentCaptor.getAllValues().get(0).toString(), is(EMPTY_JSON.toString()));
        then(opHTTPClient).should().callToken(
                TestConstants.TOKEN_COMPLETE_URI,
                "MY_CODE",
                "https://rp-example.com/callback",
                TestConstants.SIGNED_JWT_AUTH,
                null
        );
        then(tokenHandler).should().handleTokenResponse(TestConstants.TOKEN_HTTP_RESPONSE);
        then(signatureHelper).should().verifySignature(TestConstants.JWKS, TestConstants.SIGNED_ID_TOKEN);
    }

    @Test
    public void tokenShouldSucceedWithNonce() throws Exception {
        given(randomHelper.randomString())
                .willReturn(TestConstants.TEST_UUID);
        given(signatureHelper.createJWT(
                any(),
                anyString(),
                anyString(),
                anyString(),
                anyLong(),
                anyString(),
                anyString(),
                any()))
                .willReturn(TestConstants.SIGNED_JWT_AUTH);
        given(opHTTPClient.callToken(anyString(), anyString(), anyString(), anyString(), any()))
                .willReturn(TestConstants.TOKEN_HTTP_RESPONSE);
        given(tokenHandler.handleTokenResponse(anyString()))
                .willReturn(TestConstants.SIGNED_ID_TOKEN);
        given(signatureHelper.verifySignature(any(), any()))
                .willReturn(true);
        given(tokenHandler.extractTokenData(any()))
                .willReturn(TestConstants.ID_TOKEN_WITH_NONCE);

        FieldUtils.writeDeclaredField(verifiedIdClientImp, "opConfiguration", TestConstants.OP_CONFIGURATION_WITH_JWKS, true);

        TokenRequest tokenRequest = TokenRequest.builder()
                .redirectUri("https://rp-example.com/callback")
                .authorizationCode("MY_CODE")
                .nonce(TestConstants.NONCE)
                .build();

        IdToken idToken = verifiedIdClientImp.token(tokenRequest);

        assertThat("Wrong token", idToken, is(TestConstants.ID_TOKEN_WITH_NONCE));

        then(randomHelper).should().randomString();
        ArgumentCaptor<JSONObject> stringArgumentCaptor = ArgumentCaptor.forClass(JSONObject.class);
        then(signatureHelper).should().createJWT(
                eq(TestConstants.PRIVATE_JWK),
                eq("RS256"),
                eq(TestConstants.CLIENT_ID),
                eq(TestConstants.ISSUER),
                eq(30L),
                eq(TestConstants.CLIENT_ID),
                eq(TestConstants.TEST_UUID),
                stringArgumentCaptor.capture()
        );
        assertThat("wrong auth payload", stringArgumentCaptor.getAllValues().get(0).toString(), is(EMPTY_JSON.toString()));
        then(opHTTPClient).should().callToken(
                TestConstants.TOKEN_COMPLETE_URI,
                "MY_CODE",
                "https://rp-example.com/callback",
                TestConstants.SIGNED_JWT_AUTH,
                null
        );
        then(tokenHandler).should().handleTokenResponse(TestConstants.TOKEN_HTTP_RESPONSE);
        then(signatureHelper).should().verifySignature(TestConstants.JWKS, TestConstants.SIGNED_ID_TOKEN);
    }

    @Test
    public void tokenShouldSucceedWithCodeVerifier() throws Exception {
        given(randomHelper.randomString())
                .willReturn(TestConstants.TEST_UUID);
        given(signatureHelper.createJWT(
                any(),
                anyString(),
                anyString(),
                anyString(),
                anyLong(),
                anyString(),
                anyString(),
                any()))
                .willReturn(TestConstants.SIGNED_JWT_AUTH);
        given(opHTTPClient.callToken(anyString(), anyString(), anyString(), anyString(), any()))
                .willReturn(TestConstants.TOKEN_HTTP_RESPONSE);
        given(tokenHandler.handleTokenResponse(anyString()))
                .willReturn(TestConstants.SIGNED_ID_TOKEN);
        given(signatureHelper.verifySignature(any(), any()))
                .willReturn(true);
        given(tokenHandler.extractTokenData(any()))
                .willReturn(TestConstants.ID_TOKEN_WITH_NONCE);

        FieldUtils.writeDeclaredField(verifiedIdClientImp, "opConfiguration", TestConstants.OP_CONFIGURATION_WITH_JWKS, true);

        TokenRequest tokenRequest = TokenRequest.builder()
                .redirectUri("https://rp-example.com/callback")
                .authorizationCode("MY_CODE")
                .nonce(TestConstants.NONCE)
                .codeVerifier(TestConstants.TEST_CODE_VERIFIER)
                .build();

        IdToken idToken = verifiedIdClientImp.token(tokenRequest);

        assertThat("Wrong token", idToken, is(TestConstants.ID_TOKEN_WITH_NONCE));

        then(randomHelper).should().randomString();
        ArgumentCaptor<JSONObject> stringArgumentCaptor = ArgumentCaptor.forClass(JSONObject.class);
        then(signatureHelper).should().createJWT(
                eq(TestConstants.PRIVATE_JWK),
                eq("RS256"),
                eq(TestConstants.CLIENT_ID),
                eq(TestConstants.ISSUER),
                eq(30L),
                eq(TestConstants.CLIENT_ID),
                eq(TestConstants.TEST_UUID),
                stringArgumentCaptor.capture()
        );
        assertThat("wrong auth payload", stringArgumentCaptor.getAllValues().get(0).toString(), is(EMPTY_JSON.toString()));
        then(opHTTPClient).should().callToken(
                TestConstants.TOKEN_COMPLETE_URI,
                "MY_CODE",
                "https://rp-example.com/callback",
                TestConstants.SIGNED_JWT_AUTH,
                TestConstants.TEST_CODE_VERIFIER
        );
        then(tokenHandler).should().handleTokenResponse(TestConstants.TOKEN_HTTP_RESPONSE);
        then(signatureHelper).should().verifySignature(TestConstants.JWKS, TestConstants.SIGNED_ID_TOKEN);
    }

    @Test
    public void tokenShouldFailIfSignatureFails() throws Exception {
        given(randomHelper.randomString())
                .willReturn(TestConstants.TEST_UUID);
        given(signatureHelper.createJWT(
                any(),
                anyString(),
                anyString(),
                anyString(),
                anyLong(),
                anyString(),
                anyString(),
                any()))
                .willReturn(TestConstants.SIGNED_JWT_AUTH);
        given(opHTTPClient.callToken(anyString(), anyString(), anyString(), anyString(), any()))
                .willReturn(TestConstants.TOKEN_HTTP_RESPONSE);
        given(tokenHandler.handleTokenResponse(anyString()))
                .willReturn(TestConstants.SIGNED_ID_TOKEN);
        given(signatureHelper.verifySignature(any(), any()))
                .willReturn(false);

        FieldUtils.writeDeclaredField(verifiedIdClientImp, "opConfiguration", TestConstants.OP_CONFIGURATION_WITH_JWKS, true);

        TokenRequest tokenRequest = TokenRequest.builder()
                .redirectUri("https://rp-example.com/callback")
                .authorizationCode("MY_CODE")
                .build();

        assertThatThrows(
                () -> verifiedIdClientImp.token(tokenRequest),
                allOf(
                        isA(DigitalIdGenericException.class),
                        hasProperty("message", is("invalid signature"))
                )
        );

        then(randomHelper).should().randomString();
        ArgumentCaptor<JSONObject> stringArgumentCaptor = ArgumentCaptor.forClass(JSONObject.class);
        then(signatureHelper).should().createJWT(
                eq(TestConstants.PRIVATE_JWK),
                eq("RS256"),
                eq(TestConstants.CLIENT_ID),
                eq(TestConstants.ISSUER),
                eq(30L),
                eq(TestConstants.CLIENT_ID),
                eq(TestConstants.TEST_UUID),
                stringArgumentCaptor.capture()
        );
        assertThat("wrong auth payload", stringArgumentCaptor.getAllValues().get(0).toString(), is(EMPTY_JSON.toString()));
        then(opHTTPClient).should().callToken(
                TestConstants.TOKEN_COMPLETE_URI,
                "MY_CODE",
                "https://rp-example.com/callback",
                TestConstants.SIGNED_JWT_AUTH,
                null
        );
        then(tokenHandler).should().handleTokenResponse(TestConstants.TOKEN_HTTP_RESPONSE);
        then(signatureHelper).should().verifySignature(TestConstants.JWKS, TestConstants.SIGNED_ID_TOKEN);
        then(tokenHandler).should(never()).extractTokenData(any());
    }

    @Test
    public void tokenShouldFailIfNonceMismatches() throws Exception {
        given(randomHelper.randomString())
                .willReturn(TestConstants.TEST_UUID);
        given(signatureHelper.createJWT(
                any(),
                anyString(),
                anyString(),
                anyString(),
                anyLong(),
                anyString(),
                anyString(),
                any()))
                .willReturn(TestConstants.SIGNED_JWT_AUTH);
        given(opHTTPClient.callToken(anyString(), anyString(), anyString(), anyString(), any()))
                .willReturn(TestConstants.TOKEN_HTTP_RESPONSE);
        given(tokenHandler.handleTokenResponse(anyString()))
                .willReturn(TestConstants.SIGNED_ID_TOKEN);
        given(signatureHelper.verifySignature(any(), any()))
                .willReturn(true);
        given(tokenHandler.extractTokenData(any()))
                .willReturn(TestConstants.ID_TOKEN_WITH_NONCE);

        FieldUtils.writeDeclaredField(verifiedIdClientImp, "opConfiguration", TestConstants.OP_CONFIGURATION_WITH_JWKS, true);

        TokenRequest tokenRequest = TokenRequest.builder()
                .redirectUri("https://rp-example.com/callback")
                .authorizationCode("MY_CODE")
                .nonce("wrong-nonce")
                .build();

        assertThatThrows(
                () -> verifiedIdClientImp.token(tokenRequest),
                allOf(isA(DigitalIdGenericException.class), hasProperty("message", is("nonce mismatches")))
        );


        then(randomHelper).should().randomString();
        ArgumentCaptor<JSONObject> stringArgumentCaptor = ArgumentCaptor.forClass(JSONObject.class);
        then(signatureHelper).should().createJWT(
                eq(TestConstants.PRIVATE_JWK),
                eq("RS256"),
                eq(TestConstants.CLIENT_ID),
                eq(TestConstants.ISSUER),
                eq(30L),
                eq(TestConstants.CLIENT_ID),
                eq(TestConstants.TEST_UUID),
                stringArgumentCaptor.capture()
        );
        assertThat("wrong auth payload", stringArgumentCaptor.getAllValues().get(0).toString(), is(EMPTY_JSON.toString()));
        then(opHTTPClient).should().callToken(
                TestConstants.TOKEN_COMPLETE_URI,
                "MY_CODE",
                "https://rp-example.com/callback",
                TestConstants.SIGNED_JWT_AUTH,
                null
        );
        then(tokenHandler).should().handleTokenResponse(TestConstants.TOKEN_HTTP_RESPONSE);
        then(tokenHandler).should().extractTokenData(TestConstants.SIGNED_ID_TOKEN);
        then(signatureHelper).should().verifySignature(TestConstants.JWKS, TestConstants.SIGNED_ID_TOKEN);
    }
}