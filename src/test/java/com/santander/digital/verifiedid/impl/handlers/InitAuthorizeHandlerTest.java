package com.santander.digital.verifiedid.impl.handlers;

import org.junit.Before;
import org.junit.Test;
import com.santander.digital.verifiedid.model.init.authorize.InitiateAuthorizeResponse;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static com.santander.digital.verifiedid.TestConstants.*;

public class InitAuthorizeHandlerTest {

    private InitAuthorizeHandler initAuthorizeHandler;

    @Before
    public void before () {
        initAuthorizeHandler = new InitAuthorizeHandler();
    }

    @Test
    public void handleInitAuthorizeShouldPass () {
        InitiateAuthorizeResponse initiateAuthorizeResponse = initAuthorizeHandler.handleInitAuthorize(INITIATE_AUTHORIZE_HTTP_RESPONSE, NONCE, AUTHORIZE_COMPLETE_URI, TEST_CODE_VERIFIER);

        assertThat("wrong redirect URI", initiateAuthorizeResponse.getRedirectionUri(), is(REDIRECTION_URI));
        assertThat("wrong code verifier", initiateAuthorizeResponse.getCodeVerifier(), is(TEST_CODE_VERIFIER));
        assertThat("wrong urn", initiateAuthorizeResponse.getRequestObjectUri(), is(URN));
        assertThat("wrong nonce", initiateAuthorizeResponse.getNonce(), is(NONCE));
        assertThat("wrong expiration", initiateAuthorizeResponse.getExpiration(), is(1200008L));
    }

}