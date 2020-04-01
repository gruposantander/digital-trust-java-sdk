package com.santander.digital.verifiedid.model.claims.verifying;


import com.santander.digital.verifiedid.TestConstants;
import com.santander.digital.verifiedid.model.claims.IAL;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;
import java.util.Date;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class AssertionClaimsTest {

    private final Date now = Date.from(Instant.now());
    private final Date tomorrow = Date.from(Instant.now().plus(1, DAYS));

    private AssertionClaim<String> emailAssertionClaimEq
            = new AssertionClaimSimple<String>("email").eq("davide.bianchini@santander.co.uk");
    private AssertionClaim<String> emailAssertionClaimEqWithIAL
            = new AssertionClaimSimple<String>("email").eq("davide.bianchini@santander.co.uk").withIAL(IAL.ONE);
    private AssertionClaim<String> emailAssertionClaimEqWithEssential
            = new AssertionClaimSimple<String>("email").eq("davide.bianchini@santander.co.uk").withEssential(true);
    private AssertionClaim<String> familyNameAssertionClaimEq
            = new AssertionClaimSimple<String>("family_name").eq("Smith");
    private AssertionClaim<String> givenNameAssertionClaimEq
            = new AssertionClaimSimple<String>("given_name").eq("Paul");
    private AssertionClaim<Date> dateAssertionClaimEq
            = new AssertionClaimSimple<Date>("birthdate").eq(now);
    private AssertionClaim<Date> dateAssertionClaimGt
            = new AssertionClaimComparator<Date>("birthdate").gt(now);
    private AssertionClaim<Date> dateAssertionClaimGtWithPurpose
            = new AssertionClaimComparator<Date>("birthdate").gt(now).withPurpose("Here is a purpose");
    private AssertionClaim<Date> dateAssertionClaimGte
            = new AssertionClaimComparator<Date>("birthdate").gte(now);
    private AssertionClaim<Date> dateAssertionClaimLt
            = new AssertionClaimComparator<Date>("birthdate").lt(now);
    private AssertionClaim<Date> dateAssertionClaimLte
            = new AssertionClaimComparator<Date>("birthdate").lte(now);
    private AssertionClaim<Integer> ageAssertionClaimWithGt
            = new AssertionClaimComparator<Integer>("age").gt(18);
    private AssertionClaimComplex addressAssertionClaimWithFormatted
            = new AssertionClaimComplex("address");
    private AssertionClaimComplex addressAssertionClaimWithStreetAddress
            = new AssertionClaimComplex("address");
    private AssertionClaimComplex addressAssertionClaimWithPostalcode
            = new AssertionClaimComplex("address");
    private AssertionClaimComplex addressAssertionClaimWithLocality
            = new AssertionClaimComplex("address");
    private AssertionClaimComplex addressAssertionClaimWithRegion
            = new AssertionClaimComplex("address");
    private AssertionClaimComplex addressAssertionClaimWithCountry
            = new AssertionClaimComplex("address");
    private AssertionClaimComplex addressAssertionClaimWithPostalCodeAndCountry
            = new AssertionClaimComplex("address");

    @Before
    public void before() {
        addressAssertionClaimWithFormatted
                .withAssertion(Address.formatted().eq("1 Avebury road \nDoncaster \nSouth Yorkshire \nDC10 7EW \nUnited Kingdom"));
        addressAssertionClaimWithStreetAddress
                .withAssertion(Address.streetAddress().eq("Avebury road, 1"));
        addressAssertionClaimWithPostalcode
                .withAssertion(Address.postalCode().eq("DC10 7EW"));
        addressAssertionClaimWithLocality
                .withAssertion(Address.locality().eq("Doncaster"));
        addressAssertionClaimWithRegion
                .withAssertion(Address.region().eq("South Yorkshire"));
        addressAssertionClaimWithCountry
                .withAssertion(Address.country().eq("United Kingdom"));
        addressAssertionClaimWithPostalCodeAndCountry
                .withAssertion(Address.country().eq("United Kingdom"))
                .withAssertion(Address.postalCode().eq("DC10 7EW"))
                .withPurpose("address purpose");
    }


    @Test
    public void addEmailWithEqualsShouldAddEmailClaimIfNotAlreadyPresent() {
        AssertionClaims assertionClaims = new AssertionClaims();
        assertionClaims.email().eq("davide.bianchini@santander.co.uk");

        assertThat("Assertion not added", assertionClaims.getClaims().size(), is(1));
        assertThat("Wrong assertion added", assertionClaims.getClaims().get(0), is(emailAssertionClaimEq));
    }

    @Test
    public void addEmailWithEqualsAndIALShouldAddEmailClaimIfNotAlreadyPresent() {
        AssertionClaims assertionClaims = new AssertionClaims();
        assertionClaims.email().eq("davide.bianchini@santander.co.uk").withIAL(IAL.ONE);

        assertThat("Assertion not added", assertionClaims.getClaims().size(), is(1));
        assertThat("Wrong assertion added", assertionClaims.getClaims().get(0), is(emailAssertionClaimEqWithIAL));
    }

    @Test
    public void addEmailWithEqualsAndEssentialShouldAddEmailClaimIfNotAlreadyPresent() {
        AssertionClaims assertionClaims = new AssertionClaims();
        assertionClaims.email().eq("davide.bianchini@santander.co.uk").withEssential(true);

        assertThat("Assertion not added", assertionClaims.getClaims().size(), is(1));
        assertThat("Wrong assertion added", assertionClaims.getClaims().get(0), is(emailAssertionClaimEqWithEssential));
    }

    @Test
    public void addFamilyNameWithEqualsShouldAddFamilyNameClaimIfNotAlreadyPresent() {
        AssertionClaims assertionClaims = new AssertionClaims();
        assertionClaims.familyName().eq("Smith");

        assertThat("Assertion not added", assertionClaims.getClaims().size(), is(1));
        assertThat("Wrong assertion added", assertionClaims.getClaims().get(0), is(familyNameAssertionClaimEq));
    }

    @Test
    public void addGivenNameWithEqualsShouldAddGivenNameClaimIfNotAlreadyPresent() {
        AssertionClaims assertionClaims = new AssertionClaims();
        assertionClaims.givenName().eq("Paul");

        assertThat("Assertion not added", assertionClaims.getClaims().size(), is(1));
        assertThat("Wrong assertion added", assertionClaims.getClaims().get(0), is(givenNameAssertionClaimEq));
    }

    @Test
    public void addDateWithEqualsShouldAddDateClaimWhenEmailClaimIsAlreadyPresentIfNotAlreadyPresent() {
        AssertionClaims assertionClaims = new AssertionClaims();
        assertionClaims.email().eq("davide.bianchini@santander.co.uk");
        assertionClaims.birthdate().eq(now);

        assertThat("Assertion not added", assertionClaims.getClaims().size(), is(2));
        assertThat("Wrong assertion added", assertionClaims.getClaims().get(0), is(emailAssertionClaimEq));
        assertThat("Wrong assertion added", assertionClaims.getClaims().get(1), is(dateAssertionClaimEq));
    }

    @Test
    public void addDateWithEqualsShouldNotAddDateClaimWhenDateClaimIsAlreadyPresent() {
        AssertionClaims assertionClaims = new AssertionClaims();
        assertionClaims.birthdate().eq(tomorrow);
        assertionClaims.birthdate().eq(now);

        assertThat("Assertion not added", assertionClaims.getClaims().size(), is(1));
        assertThat("Wrong assertion added", assertionClaims.getClaims().get(0), is(dateAssertionClaimEq));
    }

    @Test
    public void addDateWithGTShouldAddDateClaimWhenDateClaimIsNotAlreadyPresent() {
        AssertionClaims assertionClaims = new AssertionClaims();
        assertionClaims.birthdate().gt(now);

        assertThat("Assertion not added", assertionClaims.getClaims().size(), is(1));
        assertThat("Wrong assertion added", assertionClaims.getClaims().get(0), is(dateAssertionClaimGt));
    }

    @Test
    public void addDateWithGTThenPurposeShouldAddDateClaimWhenDateClaimIsNotAlreadyPresent() {
        AssertionClaims assertionClaims = new AssertionClaims();
        assertionClaims.birthdate().gt(now).withPurpose("Here is a purpose");

        assertThat("Assertion not added", assertionClaims.getClaims().size(), is(1));
        assertThat("Wrong assertion added", assertionClaims.getClaims().get(0), is(dateAssertionClaimGtWithPurpose));
    }

    @Test
    public void addDateWithGTEThenPurposeShouldAddDateClaimWhenDateClaimIsNotAlreadyPresent() {
        AssertionClaims assertionClaims = new AssertionClaims();
        assertionClaims.birthdate().gte(now);

        assertThat("Assertion not added", assertionClaims.getClaims().size(), is(1));
        assertThat("Wrong assertion added", assertionClaims.getClaims().get(0), is(dateAssertionClaimGte));
    }

    @Test
    public void addDateWithLTThenPurposeShouldAddDateClaimWhenDateClaimIsNotAlreadyPresent() {
        AssertionClaims assertionClaims = new AssertionClaims();
        assertionClaims.birthdate().lt(now);

        assertThat("Assertion not added", assertionClaims.getClaims().size(), is(1));
        assertThat("Wrong assertion added", assertionClaims.getClaims().get(0), is(dateAssertionClaimLt));
    }

    @Test
    public void addDateWithLTEThenPurposeShouldAddDateClaimWhenDateClaimIsNotAlreadyPresent() {
        AssertionClaims assertionClaims = new AssertionClaims();
        assertionClaims.birthdate().lte(now);

        assertThat("Assertion not added", assertionClaims.getClaims().size(), is(1));
        assertThat("Wrong assertion added", assertionClaims.getClaims().get(0), is(dateAssertionClaimLte));
    }

    @Test
    public void addAgeWithGTShouldAddAgeClaimWhenDateClaimIsNotAlreadyPresent() {
        AssertionClaims assertionClaims = new AssertionClaims();
        assertionClaims.age().gt(18);

        assertThat("Assertion not added", assertionClaims.getClaims().size(), is(1));
        assertThat("Wrong assertion added", assertionClaims.getClaims().get(0), is(ageAssertionClaimWithGt));
    }

    @Test
    public void addGenderShouldAddClaimWhenNotAlreadyPresent() {
        AssertionClaims assertionClaims = new AssertionClaims();
        assertionClaims.gender().eq("female");

        assertThat("Assertion not added", assertionClaims.getClaims().size(), is(1));
        assertThat("Wrong assertion added", assertionClaims.getClaims().get(0), is(getSimpleAssertionClaim("gender", "female")));
    }

    @Test
    public void addCOBShouldAddClaimWhenNotAlreadyPresent() {
        AssertionClaims assertionClaims = new AssertionClaims();
        assertionClaims.countryOfBirth().eq("GB");

        assertThat("Assertion not added", assertionClaims.getClaims().size(), is(1));
        assertThat("Wrong assertion added", assertionClaims.getClaims().get(0), is(getSimpleAssertionClaim("country_of_birth", "GB")));
    }

    @Test
    public void addTitleShouldAddClaimWhenNotAlreadyPresent() {
        AssertionClaims assertionClaims = new AssertionClaims();
        assertionClaims.countryOfBirth().eq("GB");

        assertThat("Assertion not added", assertionClaims.getClaims().size(), is(1));
        assertThat("Wrong assertion added", assertionClaims.getClaims().get(0), is(getSimpleAssertionClaim("country_of_birth", "GB")));
    }


    @Test
    public void addNationalityShouldAddClaimWhenNotAlreadyPresent() {
        AssertionClaims assertionClaims = new AssertionClaims();
        assertionClaims.nationality().eq("GB");

        assertThat("Assertion not added", assertionClaims.getClaims().size(), is(1));
        assertThat("Wrong assertion added", assertionClaims.getClaims().get(0), is(getSimpleAssertionClaim("nationality", "GB")));
    }

    @Test
    public void addCivilStatusShouldAddClaimWhenNotAlreadyPresent() {
        AssertionClaims assertionClaims = new AssertionClaims();
        assertionClaims.civilStatus().eq("married");

        assertThat("Assertion not added", assertionClaims.getClaims().size(), is(1));
        assertThat("Wrong assertion added", assertionClaims.getClaims().get(0), is(getSimpleAssertionClaim("civil_status", "married")));
    }

    @Test
    public void addAgeShouldAddClaimWhenNotAlreadyPresent() {
        AssertionClaims assertionClaims = new AssertionClaims();
        assertionClaims.age().eq(18);

        assertThat("Assertion not added", assertionClaims.getClaims().size(), is(1));
        assertThat("Wrong assertion added", assertionClaims.getClaims().get(0), is(getSimpleAssertionClaim("age", 18)));
    }


    @Test
    public void addCompanyRegisteredNameShouldAddClaimWhenNotAlreadyPresent() {
        AssertionClaims assertionClaims = new AssertionClaims();
        assertionClaims.companyRegisteredName().eq("acme");

        assertThat("Assertion not added", assertionClaims.getClaims().size(), is(1));
        assertThat("Wrong assertion added", assertionClaims.getClaims().get(0), is(getSimpleAssertionClaim("company_registered_name", "acme")));
    }


    @Test
    public void addCompanyTradeNameShouldAddClaimWhenNotAlreadyPresent() {
        AssertionClaims assertionClaims = new AssertionClaims();
        assertionClaims.companyTradeName().eq("acme");

        assertThat("Assertion not added", assertionClaims.getClaims().size(), is(1));
        assertThat("Wrong assertion added", assertionClaims.getClaims().get(0), is(getSimpleAssertionClaim("company_trade_name", "acme")));
    }

    @Test
    public void addCompanyStartDateShouldAddClaimWhenNotAlreadyPresent() {
        AssertionClaims assertionClaims = new AssertionClaims();
        assertionClaims.companyStartDate().eq(TestConstants.FIFTH_OF_JANUARY_2019);

        assertThat("Assertion not added", assertionClaims.getClaims().size(), is(1));
        assertThat("Wrong assertion added", assertionClaims.getClaims().get(0), is(getSimpleAssertionClaim("company_start_date", TestConstants.FIFTH_OF_JANUARY_2019)));
    }

    @Test
    public void addCompanyEndDateShouldAddClaimWhenNotAlreadyPresent() {
        AssertionClaims assertionClaims = new AssertionClaims();
        assertionClaims.companyEndDate().eq(TestConstants.FIFTH_OF_JANUARY_2019);

        assertThat("Assertion not added", assertionClaims.getClaims().size(), is(1));
        assertThat("Wrong assertion added", assertionClaims.getClaims().get(0), is(getSimpleAssertionClaim("company_end_date", TestConstants.FIFTH_OF_JANUARY_2019)));
    }

    @Test
    public void addCompanyTypeShouldAddClaimWhenNotAlreadyPresent() {
        AssertionClaims assertionClaims = new AssertionClaims();
        assertionClaims.companyType().eq("sole trader");

        assertThat("Assertion not added", assertionClaims.getClaims().size(), is(1));
        assertThat("Wrong assertion added", assertionClaims.getClaims().get(0), is(getSimpleAssertionClaim("company_type", "sole trader")));
    }

    @Test
    public void addCompanyCountryIncorporationShouldAddClaimWhenNotAlreadyPresent() {
        AssertionClaims assertionClaims = new AssertionClaims();
        assertionClaims.companyCountryIncorporation().eq("GB");

        assertThat("Assertion not added", assertionClaims.getClaims().size(), is(1));
        assertThat("Wrong assertion added", assertionClaims.getClaims().get(0), is(getSimpleAssertionClaim("company_country_incorporation", "GB")));
    }

    @Test
    public void addCompanyAgeShouldAddClaimWhenNotAlreadyPresent() {
        AssertionClaims assertionClaims = new AssertionClaims();
        assertionClaims.companyAge().eq(3);

        assertThat("Assertion not added", assertionClaims.getClaims().size(), is(1));
        assertThat("Wrong assertion added", assertionClaims.getClaims().get(0), is(getSimpleAssertionClaim("company_age", 3)));
    }

    @Test
    public void addCompanyOperatingShouldAddClaimWhenNotAlreadyPresent() {
        AssertionClaims assertionClaims = new AssertionClaims();
        assertionClaims.companyOperating().eq(false);

        assertThat("Assertion not added", assertionClaims.getClaims().size(), is(1));
        assertThat("Wrong assertion added", assertionClaims.getClaims().get(0), is(getSimpleAssertionClaim("company_operating", false)));
    }


    @Test
    public void addAddressWithFormattedShouldAddAddressClaimWhenAddressClaimIsNotAlreadyPresent() {
        AssertionClaims assertionClaims = new AssertionClaims();
        assertionClaims.address()
                .withAssertion(Address.formatted().eq("1 Avebury road \nDoncaster \nSouth Yorkshire \nDC10 7EW \nUnited Kingdom"));

        assertThat("Assertion not added", assertionClaims.getClaims().size(), is(1));
        assertThat("Wrong assertion added", assertionClaims.getClaims().get(0), is(addressAssertionClaimWithFormatted));
    }

    @Test
    public void addAddressWithFormattedReplaceAddAddressClaimWhenAddressClaimIsAlreadyPresent() {
        AssertionClaims assertionClaims = new AssertionClaims();
        assertionClaims.address()
                .withAssertion(Address.formatted().eq("10 St Gilbert Street Northampton NN1 3AH United Kingdom"));
        assertionClaims.address()
                .withAssertion(Address.formatted().eq("1 Avebury road \nDoncaster \nSouth Yorkshire \nDC10 7EW \nUnited Kingdom"));

        assertThat("Assertion not added", assertionClaims.getClaims().size(), is(1));
        assertThat("Wrong assertion added", assertionClaims.getClaims().get(0), is(addressAssertionClaimWithFormatted));
    }

    @Test
    public void addAddressWithStreetAddressShouldAddAddressClaimWhenAddressClaimIsNotAlreadyPresent() {
        AssertionClaims assertionClaims = new AssertionClaims();
        assertionClaims.address()
                .withAssertion(Address.streetAddress().eq("Avebury road, 1"));

        assertThat("Assertion not added", assertionClaims.getClaims().size(), is(1));
        assertThat("Wrong assertion added", assertionClaims.getClaims().get(0), is(addressAssertionClaimWithStreetAddress));
    }

    @Test
    public void addAddressWithPostalCodeShouldAddAddressClaimWhenAddressClaimIsNotAlreadyPresent() {
        AssertionClaims assertionClaims = new AssertionClaims();
        assertionClaims.address()
                .withAssertion(Address.postalCode().eq("DC10 7EW"));

        assertThat("Assertion not added", assertionClaims.getClaims().size(), is(1));
        assertThat("Wrong assertion added", assertionClaims.getClaims().get(0), is(addressAssertionClaimWithPostalcode));
    }

    @Test
    public void addAddressWithLocalityShouldAddAddressClaimWhenAddressClaimIsNotAlreadyPresent() {
        AssertionClaims assertionClaims = new AssertionClaims();
        assertionClaims.address()
                .withAssertion(Address.locality().eq("Doncaster"));

        assertThat("Assertion not added", assertionClaims.getClaims().size(), is(1));
        assertThat("Wrong assertion added", assertionClaims.getClaims().get(0), is(addressAssertionClaimWithLocality));
    }

    @Test
    public void addAddressWithRegionShouldAddAddressClaimWhenAddressClaimIsNotAlreadyPresent() {
        AssertionClaims assertionClaims = new AssertionClaims();
        assertionClaims.address()
                .withAssertion(Address.region().eq("South Yorkshire"));

        assertThat("Assertion not added", assertionClaims.getClaims().size(), is(1));
        assertThat("Wrong assertion added", assertionClaims.getClaims().get(0), is(addressAssertionClaimWithRegion));
    }

    @Test
    public void addAddressWithCountryShouldAddAddressClaimWhenAddressClaimIsNotAlreadyPresent() {
        AssertionClaims assertionClaims = new AssertionClaims();
        assertionClaims.address()
                .withAssertion(Address.country().eq("United Kingdom"));

        assertThat("Assertion not added", assertionClaims.getClaims().size(), is(1));
        assertThat("Wrong assertion added", assertionClaims.getClaims().get(0), is(addressAssertionClaimWithCountry));
    }

    @Test
    public void addAddressWithCountryAndPostalCodeShouldAddAddressClaimWhenAddressClaimIsNotAlreadyPresent() {
        AssertionClaims assertionClaims = new AssertionClaims();
        assertionClaims.address()
                .withAssertion(Address.country().eq("United Kingdom"))
                .withAssertion(Address.postalCode().eq("DC10 7EW"))
                .withPurpose("address purpose");

        assertThat("Assertion not added", assertionClaims.getClaims().size(), is(1));
        assertThat("Wrong assertion added", assertionClaims.getClaims().get(0), is(addressAssertionClaimWithPostalCodeAndCountry));
    }

    @Test
    public void addTotalBalanceWithGTShouldAddTotalBalanceClaimWhenTotalBalanceClaimIsNotAlreadyPresent() {
        AssertionClaims assertionClaims = new AssertionClaims();
        assertionClaims.totalBalance()
                .withAssertion(Balance.currency().eq(Currency.getInstance("GBP")))
                .withAssertion(Balance.amount().gt(BigDecimal.valueOf(99.99)));


        assertThat("Assertion not added", assertionClaims.getClaims().size(), is(1));
        assertThat("Wrong assertion added", assertionClaims.getClaims().get(0), is(getBalanceClaim("total_balance")));
    }

    @Test
    public void addLastYearMoneyInWithGTShouldAddTotalBalanceClaimWhenTotalBalanceClaimIsNotAlreadyPresent() {
        AssertionClaims assertionClaims = new AssertionClaims();
        assertionClaims.lastYearMoneyIn()
                .withAssertion(Balance.currency().eq(Currency.getInstance("GBP")))
                .withAssertion(Balance.amount().gt(BigDecimal.valueOf(99.99)));


        assertThat("Assertion not added", assertionClaims.getClaims().size(), is(1));
        assertThat("Wrong assertion added", assertionClaims.getClaims().get(0), is(getBalanceClaim("last_year_money_in")));
    }

    @Test
    public void addLastQuarterMoneyInWithGTShouldAddTotalBalanceClaimWhenTotalBalanceClaimIsNotAlreadyPresent() {
        AssertionClaims assertionClaims = new AssertionClaims();
        assertionClaims.lastQuarterMoneyIn()
                .withAssertion(Balance.currency().eq(Currency.getInstance("GBP")))
                .withAssertion(Balance.amount().gt(BigDecimal.valueOf(99.99)));


        assertThat("Assertion not added", assertionClaims.getClaims().size(), is(1));
        assertThat("Wrong assertion added", assertionClaims.getClaims().get(0), is(getBalanceClaim("last_quarter_money_in")));
    }

    @Test
    public void addAverageMonthlyMoneyInWithGTShouldAddTotalBalanceClaimWhenTotalBalanceClaimIsNotAlreadyPresent() {
        AssertionClaims assertionClaims = new AssertionClaims();
        assertionClaims.averageMonthlyMoneyIn()
                .withAssertion(Balance.currency().eq(Currency.getInstance("GBP")))
                .withAssertion(Balance.amount().gt(BigDecimal.valueOf(99.99)));


        assertThat("Assertion not added", assertionClaims.getClaims().size(), is(1));
        assertThat("Wrong assertion added", assertionClaims.getClaims().get(0), is(getBalanceClaim("average_monthly_money_in")));
    }

    @Test
    public void addPassportIdNameShouldAddClaimWhenNotAlreadyPresent() {
        AssertionClaims assertionClaims = new AssertionClaims();
        assertionClaims.passportId().eq("123AAA");

        assertThat("Assertion not added", assertionClaims.getClaims().size(), is(1));
        assertThat("Wrong assertion added", assertionClaims.getClaims().get(0), is(getSimpleAssertionClaim("passport_id", "123AAA")));
    }

    @Test
    public void addDrivingLicenceIdShouldAddClaimWhenNotAlreadyPresent() {
        AssertionClaims assertionClaims = new AssertionClaims();
        assertionClaims.drivingLicenseId().eq("123AAA");

        assertThat("Assertion not added", assertionClaims.getClaims().size(), is(1));
        assertThat("Wrong assertion added", assertionClaims.getClaims().get(0), is(getSimpleAssertionClaim("driving_license_id", "123AAA")));
    }

    @Test
    public void addNationalCardIdShouldAddClaimWhenNotAlreadyPresent() {
        AssertionClaims assertionClaims = new AssertionClaims();
        assertionClaims.nationalCardId().eq("123AAA");

        assertThat("Assertion not added", assertionClaims.getClaims().size(), is(1));
        assertThat("Wrong assertion added", assertionClaims.getClaims().get(0), is(getSimpleAssertionClaim("national_card_id", "123AAA")));
    }


    private <T> AssertionClaimSimple<T> getSimpleAssertionClaim(String name, T value) {
        return new AssertionClaimSimple<T>(name).eq(value);
    }

    private AssertionClaimComplex getBalanceClaim(String name) {
        return new AssertionClaimComplex(name)
                .withAssertion(Balance.currency().eq(Currency.getInstance("GBP")))
                .withAssertion(Balance.amount().gt(BigDecimal.valueOf(99.99)));
    }
}
