package com.santander.digital.verifiedid.model.claims.sharing;


import com.santander.digital.verifiedid.TestUtils;
import com.santander.digital.verifiedid.exceptions.DigitalIdGenericException;
import com.santander.digital.verifiedid.model.claims.IAL;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;

public class ClaimsTest {

    private Claim emailClaimSimple = new Claim("email");
    private Claim emailClaimWithPurpose = new Claim("email").withPurpose("my purpose");
    private Claim emailClaimWithEssential = new Claim("email").withEssential(true);
    private Claim emailClaimWithIAL = new Claim("email").withIAL(IAL.ONE);

    private Claim createClaim(String name) {
        return new Claim(name);
    }

    private Claims claims;

    @Before
    public void before() {
        claims = new Claims();
    }

    @Test
    public void addEmailShouldAddClaimIfNotAlreadyPresent() {
        claims.email();
        assertThat("email claim not added", claims.getClaims().get(0), is(emailClaimSimple));
    }

    @Test
    public void addGivenNameShouldAddClaimIfNotAlreadyPresent() {
        claims.givenName();
        assertThat("claim not added", claims.getClaims().get(0), is(createClaim("given_name")));
    }

    @Test
    public void addFamilyNameShouldAddClaimIfNotAlreadyPresent() {
        claims.familyName();
        assertThat("claim not added", claims.getClaims().get(0), is(createClaim("family_name")));
    }

    @Test
    public void addBirthDateShouldAddClaimIfNotAlreadyPresent() {
        claims.birthdate();
        assertThat("claim not added", claims.getClaims().get(0), is(createClaim("birthdate")));
    }

    @Test
    public void addGenderShouldAddClaimIfNotAlreadyPresent() {
        claims.gender();
        assertThat("claim not added", claims.getClaims().get(0), is(createClaim("gender")));
    }

    @Test
    public void addCountryOfBirthShouldAddClaimIfNotAlreadyPresent() {
        claims.countryOfBirth();
        assertThat("claim not added", claims.getClaims().get(0), is(createClaim("country_of_birth")));
    }

    @Test
    public void addTitleShouldAddClaimIfNotAlreadyPresent() {
        claims.title();
        assertThat("claim not added", claims.getClaims().get(0), is(createClaim("title")));
    }

    @Test
    public void addNationalityShouldAddClaimIfNotAlreadyPresent() {
        claims.nationality();
        assertThat("claim not added", claims.getClaims().get(0), is(createClaim("nationality")));
    }

    @Test
    public void addCivilStatusShouldAddClaimIfNotAlreadyPresent() {
        claims.civilStatus();
        assertThat("claim not added", claims.getClaims().get(0), is(createClaim("civil_status")));
    }

    @Test
    public void addAgeShouldAddClaimIfNotAlreadyPresent() {
        claims.age();
        assertThat("claim not added", claims.getClaims().get(0), is(createClaim("age")));
    }

    @Test
    public void addCompanyRegisteredNameShouldAddClaimIfNotAlreadyPresent() {
        claims.companyRegisteredName();
        assertThat("claim not added", claims.getClaims().get(0), is(createClaim("company_registered_name")));
    }

    @Test
    public void addCompanyTradeNameShouldAddClaimIfNotAlreadyPresent() {
        claims.companyTradeName();
        assertThat("claim not added", claims.getClaims().get(0), is(createClaim("company_trade_name")));
    }

    @Test
    public void addCompanyStartDateShouldAddClaimIfNotAlreadyPresent() {
        claims.companyStartDate();
        assertThat("claim not added", claims.getClaims().get(0), is(createClaim("company_start_date")));
    }

    @Test
    public void addCompanyEndDateShouldAddClaimIfNotAlreadyPresent() {
        claims.companyEndDate();
        assertThat("claim not added", claims.getClaims().get(0), is(createClaim("company_end_date")));
    }

    @Test
    public void addCompanyTypeShouldAddClaimIfNotAlreadyPresent() {
        claims.companyType();
        assertThat("claim not added", claims.getClaims().get(0), is(createClaim("company_type")));
    }

    @Test
    public void addCompanyCountryIncorporationShouldAddClaimIfNotAlreadyPresent() {
        claims.companyCountryIncorporation();
        assertThat("claim not added", claims.getClaims().get(0), is(createClaim("company_country_incorporation")));
    }

    @Test
    public void addCompanyAgeShouldAddClaimIfNotAlreadyPresent() {
        claims.companyAge();
        assertThat("claim not added", claims.getClaims().get(0), is(createClaim("company_age")));
    }

    @Test
    public void addCompanyOperatingShouldAddClaimIfNotAlreadyPresent() {
        claims.companyOperating();
        assertThat("claim not added", claims.getClaims().get(0), is(createClaim("company_operating")));
    }

    @Test
    public void addPhoneNumberShouldAddClaimIfNotAlreadyPresent() {
        claims.phoneNumber();
        assertThat("claim not added", claims.getClaims().get(0), is(createClaim("phone_number")));
    }

    @Test
    public void addAddressShouldAddClaimIfNotAlreadyPresent() {
        claims.address();
        assertThat("claim not added", claims.getClaims().get(0), is(createClaim("address")));
    }

    @Test
    public void addTotalBalanceShouldAddClaimIfNotAlreadyPresent() {
        claims.totalBalance();
        assertThat("claim not added", claims.getClaims().get(0), is(createClaim("total_balance")));
    }

    @Test
    public void addLastYearMoneyInShouldAddClaimIfNotAlreadyPresent() {
        claims.lastYearMoneyIn();
        assertThat("claim not added", claims.getClaims().get(0), is(createClaim("last_year_money_in")));
    }

    @Test
    public void addLastQuarterMoneyInShouldAddClaimIfNotAlreadyPresent() {
        claims.lastQuarterMoneyIn();
        assertThat("claim not added", claims.getClaims().get(0), is(createClaim("last_quarter_money_in")));
    }

    @Test
    public void addAverageMonthlyMoneyInShouldAddClaimIfNotAlreadyPresent() {
        claims.averageMonthlyMoneyIn();
        assertThat("claim not added", claims.getClaims().get(0), is(createClaim("average_monthly_money_in")));
    }

    @Test
    public void addPassportIdShouldAddClaimIfNotAlreadyPresent() {
        claims.passportId();
        assertThat("claim not added", claims.getClaims().get(0), is(createClaim("passport_id")));
    }

    @Test
    public void addDrivingLicenseIdShouldAddClaimIfNotAlreadyPresent() {
        claims.drivingLicenseId();
        assertThat("claim not added", claims.getClaims().get(0), is(createClaim("driving_license_id")));
    }

    @Test
    public void addNationalCardIdShouldAddClaimIfNotAlreadyPresent() {
        claims.nationalCardId();
        assertThat("claim not added", claims.getClaims().get(0), is(createClaim("national_card_id")));
    }


    @Test
    public void addEmailWithPurposeShouldAddEmailClaimIfNotAlreadyPresent() {
        claims.email().withPurpose("my purpose");
        assertThat("email claim not added", claims.getClaims().get(0), is(emailClaimWithPurpose));
    }

    @Test
    public void addEmailWithEssentialShouldAddEmailClaimIfNotAlreadyPresent() {
        claims.email().withEssential(true);
        assertThat("email claim not added", claims.getClaims().get(0), is(emailClaimWithEssential));
    }

    @Test
    public void addEmailWithIALShouldAddEmailClaimIfNotAlreadyPresent() {
        claims.email().withIAL(IAL.ONE);
        assertThat("email claim not added", claims.getClaims().get(0), is(emailClaimWithIAL));
    }

    @Test
    public void addEmailWithIALShouldAddEmailClaimAgeClaimWithPurposeAlreadyPresent() {
        claims.age().withPurpose("testing purpose");
        claims.email().withIAL(IAL.ONE);
        assertThat("email claim not added", claims.getClaims().get(1), is(emailClaimWithIAL));
        assertThat("email claim not added", claims.getClaims().size(), is(2));
    }

    @Test
    public void addEmailShouldAddEmailClaimAgeClaimAlreadyPresent() {
        claims.age();
        claims.email().withPurpose("my purpose");
        assertThat("email claim not added", claims.getClaims().get(1), is(emailClaimWithPurpose));
        assertThat("email claim not added", claims.getClaims().size(), is(2));
    }

    @Test
    public void addEmailShouldReplacePreviousIfAlreadyPresent() {
        claims.email();
        claims.email().withPurpose("my purpose");
        assertThat("email claim not replaced", claims.getClaims().size(), is(1));
        assertThat("email claim not replaced", claims.getClaims().get(0), is(emailClaimWithPurpose));
    }

    @Test
    public void addEmailShouldFailIfPurposeCharacterLengthExceeds300() {
        TestUtils.assertThatThrows(() -> claims.email().withPurpose(StringUtils.repeat("a", 301)),
                CoreMatchers.allOf(
                        isA(DigitalIdGenericException.class),
                        hasProperty("message", is("purpose shouldn't exceed 300 chars"))
                )
        );
        assertThat("email claim added", claims.getClaims().size(), is(0));

    }
}