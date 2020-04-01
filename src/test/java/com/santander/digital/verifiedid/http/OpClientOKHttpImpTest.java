package com.santander.digital.verifiedid.http;


import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.santander.digital.verifiedid.TestConstants;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import com.santander.digital.verifiedid.exceptions.HTTPClientException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;
import static com.santander.digital.verifiedid.TestUtils.assertThatThrows;

public class OpClientOKHttpImpTest {

    private OpClientOKHttpImp client;

    @Before
    public void before() {
        client = new OpClientOKHttpImpBuilder().build();
    }

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(9099);

    @Test
    public void callSimpleGETShouldSucceed() throws Exception {
        givenThat(any(anyUrl())
                .willReturn(
                        aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json; charset=utf-8")
                                .withBody(TestConstants.WELL_KNOWN_STRING)
                ));

        String response = this.client.callSimpleGET(TestConstants.WELL_KNOWN_COMPLETE_URI);

        assertThat("wrong return object", response, Is.is(TestConstants.WELL_KNOWN_STRING));

        verify(getRequestedFor(urlEqualTo(TestConstants.WELL_KNOWN_URL)));
    }

    @Test
    public void callSimpleGETShouldFailIf404IsReturned() throws Exception {
        givenThat(any(anyUrl())
                .willReturn(
                        aResponse()
                                .withStatus(404)
                                .withHeader("Content-Type", "application/json; charset=utf-8")
                                .withBody(TestConstants.WELL_KNOWN_STRING)
                ));

        assertThatThrows(() -> this.client.callSimpleGET(TestConstants.WELL_KNOWN_COMPLETE_URI),
                allOf(
                        isA(HTTPClientException.class),
                        hasProperty("message", is("Unexpected response code: 404"))
                ));

        verify(getRequestedFor(urlEqualTo(TestConstants.WELL_KNOWN_URL)));
    }

    @Test
    public void callSimpleGETShouldFailIfFaultHappen() throws Exception {
        givenThat(any(anyUrl())
                .willReturn(
                        aResponse()
                                .withFault(Fault.CONNECTION_RESET_BY_PEER)
                ));

        assertThatThrows(() -> this.client.callSimpleGET(TestConstants.WELL_KNOWN_COMPLETE_URI),
                allOf(
                        isA(HTTPClientException.class),
                        hasProperty("message", is("java.net.SocketException: Connection reset"))
                ));

        verify(getRequestedFor(urlEqualTo(TestConstants.WELL_KNOWN_URL)));
    }

    @Test
    public void callInitAuthoriseShouldSucceed() throws Exception {
        givenThat(any(anyUrl())
                .willReturn(
                        aResponse()
                                .withStatus(201)
                                .withHeader("Content-Type", "application/json; charset=utf-8")
                                .withBody(TestConstants.INITIATE_AUTHORIZE_HTTP_RESPONSE)
                ));

        String response = this.client.callInitAuthorize(TestConstants.INIT_AUTHORIZE_COMPLETE_URI, "claims", "auth");

        assertThat("wrong response", response, Is.is(TestConstants.INITIATE_AUTHORIZE_HTTP_RESPONSE));
        verify(
                postRequestedFor(urlEqualTo(TestConstants.INIT_AUTHORIZE_URL))
                        .withHeader("Content-Type", equalTo("application/x-www-form-urlencoded"))
                        .withRequestBody(equalTo("client_assertion_type=urn%3Aietf%3Aparams%3Aoauth%3Aclient-assertion-type%3Ajwt-bearer&client_assertion=auth&request=claims"))
        );
    }

    @Test
    public void callTokenShouldSucceedWithCodeVerifier() throws Exception {
        givenThat(any(anyUrl())
                .willReturn(
                        aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json; charset=utf-8")
                                .withBody(TestConstants.TOKEN_HTTP_RESPONSE)
                ));

        String response = this.client.callToken(TestConstants.TOKEN_COMPLETE_URI, "MY_CODE", "https://rp-callback.com", "auth", TestConstants.TEST_CODE_VERIFIER);

        assertThat("wrong response", response, Is.is(TestConstants.TOKEN_HTTP_RESPONSE));
        verify(
                postRequestedFor(urlEqualTo(TestConstants.TOKEN_URL))
                        .withHeader("Content-Type", equalTo("application/x-www-form-urlencoded"))
                        .withRequestBody(equalTo("grant_type=authorization_code&code=MY_CODE&redirect_uri=https%3A%2F%2Frp-callback.com&client_assertion_type=urn%3Aietf%3Aparams%3Aoauth%3Aclient-assertion-type%3Ajwt-bearer&client_assertion=auth&code_verifier=verifier-11111111-1111-1111-1111-111111111111"))
        );
    }

    @Test
    public void callTokenShouldSucceedWithoutCodeVerifier() throws Exception {
        givenThat(any(anyUrl())
                .willReturn(
                        aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json; charset=utf-8")
                                .withBody(TestConstants.TOKEN_HTTP_RESPONSE)
                ));

        String response = this.client.callToken(TestConstants.TOKEN_COMPLETE_URI, "MY_CODE", "https://rp-callback.com", "auth", null);

        assertThat("wrong response", response, Is.is(TestConstants.TOKEN_HTTP_RESPONSE));
        verify(
                postRequestedFor(urlEqualTo(TestConstants.TOKEN_URL))
                        .withHeader("Content-Type", equalTo("application/x-www-form-urlencoded"))
                        .withRequestBody(equalTo("grant_type=authorization_code&code=MY_CODE&redirect_uri=https%3A%2F%2Frp-callback.com&client_assertion_type=urn%3Aietf%3Aparams%3Aoauth%3Aclient-assertion-type%3Ajwt-bearer&client_assertion=auth"))
        );
    }
}