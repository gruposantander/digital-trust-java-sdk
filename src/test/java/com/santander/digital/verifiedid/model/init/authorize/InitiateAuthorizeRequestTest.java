package com.santander.digital.verifiedid.model.init.authorize;

import com.santander.digital.verifiedid.TestConstants;
import com.santander.digital.verifiedid.model.claims.sharing.Claims;
import com.santander.digital.verifiedid.model.claims.verifying.AssertionClaims;
import com.santander.digital.verifiedid.model.claims.verifying.Balance;
import org.hamcrest.CoreMatchers;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import com.santander.digital.verifiedid.model.claims.verifying.Address;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.Instant;
import java.util.Currency;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class InitiateAuthorizeRequestTest {

    private Claims claims;
    private AssertionClaims assertionClaims;

    @Before
    public void before() {
        claims = new Claims();
        claims.givenName().withEssential(true).withPurpose("Given name purpose");

        assertionClaims = new AssertionClaims();
        assertionClaims.givenName().eq("John").withPurpose("This is why RP is verifying your name");
        assertionClaims.email().eq("jane.doe@santander.co.uk").withPurpose("This is why RP is verifying your email");
        assertionClaims.birthdate().eq(Date.from(Instant.EPOCH)).withPurpose("This is why RP is verifying your DOB");
        assertionClaims.age().gt(18).withPurpose("age purpose");
        assertionClaims.address()
                .withAssertion(Address.postalCode().eq("MK1 1AA"))
                .withAssertion(Address.country().eq("United Kingdom"))
                .withPurpose("address assertion purpose")
                .withEssential(true);
        assertionClaims.totalBalance()
                .withAssertion(Balance.amount().gt(BigDecimal.valueOf(100.01)))
                .withAssertion(Balance.currency().eq(Currency.getInstance("GBP")))
                .withPurpose("total balance assertion purpose")
                .withEssential(true);
    }

    @Test
    public void toJSONProducesACorrectOutputWhenGenericPurposeIsPresent() {
        final InitiateAuthorizeRequest initiateAuthorizeRequest = InitiateAuthorizeRequest.builder()
                .state("state-123")
                .redirectUri("https://rp-example.com/callback")
                .nonce("nonce-11111111-1111-1111-1111-111111111111")
                .claims(claims)
                .assertionClaims(assertionClaims)
                .purpose("generic top level purpose")
                .build();
        final JSONObject jsonObject = initiateAuthorizeRequest.toJSON();
        assertThat("Wrong json structure", jsonObject.toString(), CoreMatchers.is(TestConstants.INITIATE_AUTHORIZE_REQUEST_BASIC_WITH_GENERIC_PURPOSE.toString()));
    }

    @Test
    public void toJSONProducesACorrectOutputWhenRedirectURIIsPresent() {
        final InitiateAuthorizeRequest initiateAuthorizeRequest = InitiateAuthorizeRequest.builder()
                .state("state-123")
                .redirectUri("https://rp-example.com/callback")
                .nonce("nonce-11111111-1111-1111-1111-111111111111")
                .claims(claims)
                .assertionClaims(assertionClaims)
                .build();
        final JSONObject jsonObject = initiateAuthorizeRequest.toJSON();
        assertThat("Wrong json structure", jsonObject.toString(), CoreMatchers.is(TestConstants.INITIATE_AUTHORIZE_REQUEST_BASIC_WITH_REDIRECT_URI.toString()));
    }

    @Test
    public void toJSONProducesACorrectOutputWhenRedirectURIIsNotPresent() {
        final InitiateAuthorizeRequest initiateAuthorizeRequest = InitiateAuthorizeRequest.builder()
                .state("state-123")
                .nonce("nonce-11111111-1111-1111-1111-111111111111")
                .claims(claims)
                .assertionClaims(assertionClaims)
                .build();
        final JSONObject jsonObject = initiateAuthorizeRequest.toJSON();
        assertThat("Wrong json structure", jsonObject.toString(), CoreMatchers.is(TestConstants.INITIATE_AUTHORIZE_REQUEST_BASIC_WITHOUT_REDIRECT_URI.toString()));
    }
}