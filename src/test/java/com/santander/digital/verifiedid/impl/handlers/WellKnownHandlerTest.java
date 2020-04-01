package com.santander.digital.verifiedid.impl.handlers;

import org.junit.Before;
import org.junit.Test;
import com.santander.digital.verifiedid.model.OPConfiguration;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static com.santander.digital.verifiedid.TestConstants.*;

public class WellKnownHandlerTest {

    private WellKnownHandler wellKnownHandler;

    @Before
    public void before () {
        wellKnownHandler = new WellKnownHandler();
    }

    @Test
    public void handleWellKnownShouldReturnCorrectOPConfiguration() {
        OPConfiguration opConfiguration = wellKnownHandler.handleWellKnown(WELL_KNOWN_STRING);

        assertThat("Null result", opConfiguration, is(notNullValue()));
        assertThat("Wrong init-authorize endpoint",  opConfiguration.getAuthorizationEndpoint(), is(AUTHORIZE_COMPLETE_URI));
        assertThat("Wrong token endpoint",  opConfiguration.getTokenEndpoint(), is(TOKEN_COMPLETE_URI));
        assertThat("Wrong issuer",  opConfiguration.getIssuer(), is(ISSUER));
        assertThat("Wrong jwk uri",  opConfiguration.getJwksUri(), is(JWKS_COMPLETE_URI));
        assertThat("Wrong jwk provider",  opConfiguration.getJwks(), is(nullValue()));
    }

    //TODO negative scenarios
}