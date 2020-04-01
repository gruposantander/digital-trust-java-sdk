package com.santander.digital.verifiedid.impl.handlers;

import com.santander.digital.verifiedid.TestConstants;
import com.santander.digital.verifiedid.TestUtils;
import com.santander.digital.verifiedid.exceptions.DigitalIdGenericException;
import com.santander.digital.verifiedid.model.token.IdToken;
import org.apache.cxf.rs.security.jose.jws.JwsCompactConsumer;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import com.santander.digital.verifiedid.model.token.AddressClaimResponse;

import java.math.BigDecimal;
import java.util.Currency;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;

public class TokenHandlerTest {

    private TokenHandler tokenHandler;

    @Before
    public void before () {
        tokenHandler = new TokenHandler();
    }

    @Test
    public void handleTokenShouldSucceed () {
        JwsCompactConsumer jwt = tokenHandler.handleTokenResponse(TestConstants.TOKEN_HTTP_RESPONSE);
        assertThat("null result", jwt, is(notNullValue()));
    }

    @Test
    public void handleTokenShouldFailIfObjectIsMalformed () {
        TestUtils.assertThatThrows(() -> tokenHandler.handleTokenResponse('{' + TestConstants.TOKEN_HTTP_RESPONSE),
                CoreMatchers.allOf(
                        isA(DigitalIdGenericException.class),
                        hasProperty("message", startsWith("com.fasterxml.jackson.core.JsonParseException: Unexpected character"))
                )
        );
    }

    @Test
    public void extractTokenDataShouldSucceed () {
        IdToken idToken = tokenHandler.extractTokenData(TestConstants.SIGNED_ID_TOKEN2);
        assertThat("wrong sub", idToken.getSub(), is("af06cec1daecb383ad2fce124c7c00fdba538f2317920fa1529a4fa034d78d4e"));
        assertThat("wrong exp", idToken.getExpiration(), is(1585760939L));
        assertThat("wrong iat", idToken.getIssuedAt(), is(1580894266L));
        assertThat("wrong email", idToken.getEmail(), is("jane.doe@op.iamid.io"));
        assertThat("wrong phoneNumber", idToken.getPhoneNumber(), is("+441111111111"));
        assertThat("wrong address", idToken.getAddress(), is(AddressClaimResponse.builder()
                .postalCode("MK1 1AA")
                .streetAddress("1 Bag End")
                .locality("Milton Keynes")
                .region("Bucks")
                .country("United Kingdom")
                .build()));
        assertThat("wrong age", idToken.getAge(), is(25));
        assertThat("wrong number of assertion claims", idToken.getAssertionClaims().size(), is(3));
        assertThat("wrong birthdate assertion claim", idToken.getAssertionClaims().get("birthdate"), CoreMatchers.is(TestConstants.FALSE_ASSERTION));
        assertThat("wrong email assertion claim", idToken.getAssertionClaims().get("email"), CoreMatchers.is(TestConstants.FALSE_ASSERTION));
        assertThat("wrong address assertion claim", idToken.getAssertionClaims().get("address"), CoreMatchers.is(TestConstants.TRUE_ASSERTION));
        assertThat("wrong nonce", idToken.getNonce(), is("nonce-5b07e7a6-fb5e-4168-9171-5d06b8359887"));
        assertThat("wrong audience", idToken.getAudience(), is("TEST-2754efa75e8c4d11a6d7f95b90cd8e40-TEST"));
        assertThat("wrong issuer", idToken.getIssuer(), is("https://op.iamid.io"));
        assertThat("wrong given name", idToken.getGivenName(), is("Jane"));
        assertThat("wrong family name", idToken.getFamilyName(), is("Doe"));
        assertThat("wrong dob", idToken.getBirthDate(), CoreMatchers.is(TestConstants.THIRTIETH_OF_JANUARY_1990));
        assertThat("wrong total balance - amount", idToken.getTotalBalanceReponse().getAmount(), is(BigDecimal.valueOf(1000)));
        assertThat("wrong total balance - currency", idToken.getTotalBalanceReponse().getCurrency(), is(Currency.getInstance("GBP")));
    }

    @Test
    public void extractTokenDataAllSharingClaimsShouldSucceed () {
        IdToken idToken = tokenHandler.extractTokenData(TestConstants.SIGNED_ID_ALL_SHARING);

        assertThat("wrong sub", idToken.getSub(), is("af06cec1daecb383ad2fce124c7c00fdba538f2317920fa1529a4fa034d78d4e"));
        assertThat("wrong exp", idToken.getExpiration(), is(1585761106L));
        assertThat("wrong iat", idToken.getIssuedAt(), is(1580894266L));
        assertThat("wrong nonce", idToken.getNonce(), is("nonce-5b07e7a6-fb5e-4168-9171-5d06b8359887"));
        assertThat("wrong audience", idToken.getAudience(), is("TEST-2754efa75e8c4d11a6d7f95b90cd8e40-TEST"));
        assertThat("wrong issuer", idToken.getIssuer(), is("https://op.iamid.io"));

        assertThat("wrong number of assertion claims", idToken.getAssertionClaims().size(), is(0));

        assertThat("wrong given name", idToken.getGivenName(), is("Jane"));
        assertThat("wrong family name", idToken.getFamilyName(), is("Doe"));
        assertThat("wrong dob", idToken.getBirthDate(), CoreMatchers.is(TestConstants.THIRTIETH_OF_JANUARY_1990));
        assertThat("wrong gender", idToken.getGender(), is("female"));
        assertThat("wrong civil status", idToken.getCivilStatus(), is("married"));
        assertThat("wrong age", idToken.getAge(), is(25));

        assertThat("wrong parameter", idToken.getCompanyRegisteredName(), is("Acme Co"));
        assertThat("wrong parameter", idToken.getCompanyTradeName(), is("Acme"));
        assertThat("wrong parameter", idToken.getCompanyStartDate(), CoreMatchers.is(TestConstants.SECOND_OF_OCTOBER_1981));
        assertThat("wrong parameter", idToken.getCompanyEndDate(), CoreMatchers.is(TestConstants.FIFTH_OF_JANUARY_2019));
        assertThat("wrong parameter", idToken.getCompanyType(), is("Sole Trader"));
        assertThat("wrong parameter", idToken.getCompanyCountryIncorporation(), is("UK"));
        assertThat("wrong parameter", idToken.getCompanyAge(), is(3));
        assertThat("wrong parameter", idToken.getCompanyOperating(), is(true));

        assertThat("wrong email", idToken.getEmail(), is("jane.doe@op.iamid.io"));
        assertThat("wrong phoneNumber", idToken.getPhoneNumber(), is("+441111111111"));
        assertThat("wrong address", idToken.getAddress(), is(AddressClaimResponse.builder()
                .postalCode("MK1 1AA")
                .streetAddress("1, Bag End")
                .locality("Milton Keynes")
                .region("Bucks")
                .country("United Kingdom")
                .build()));

        assertThat("wrong total balance - amount", idToken.getTotalBalanceReponse().getAmount(), is(BigDecimal.valueOf(1000)));
        assertThat("wrong total balance - currency", idToken.getTotalBalanceReponse().getCurrency(), is(Currency.getInstance("GBP")));
        assertThat("wrong last year - amount", idToken.getLastYearMoneyIn().getAmount(), is(BigDecimal.valueOf(10000)));
        assertThat("wrong last year - currency", idToken.getLastYearMoneyIn().getCurrency(), is(Currency.getInstance("EUR")));
        assertThat("wrong last quarter - amount", idToken.getLastQuarterMoneyIn().getAmount(), is(BigDecimal.valueOf(3000)));
        assertThat("wrong last quarter - currency", idToken.getLastQuarterMoneyIn().getCurrency(), is(Currency.getInstance("USD")));
        assertThat("wrong average month - amount", idToken.getAverageMonthlyMoneyIn().getAmount(), is(BigDecimal.valueOf(999)));
        assertThat("wrong average month - currency", idToken.getAverageMonthlyMoneyIn().getCurrency(), is(Currency.getInstance("CAD")));
    }

}