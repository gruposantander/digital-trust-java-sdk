package com.santander.digital.verifiedid.model.claims.verifying;


import com.santander.digital.verifiedid.exceptions.DigitalIdGenericException;
import lombok.Getter;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class AssertionClaims {
    @Getter
    private List<AssertionClaim> claims = new ArrayList<>();

    public AssertionClaimSimple<String> givenName() {
        return this.addSimpleAssertionClaim("given_name");
    }

    public AssertionClaimSimple<String> familyName() {
        return this.addSimpleAssertionClaim("family_name");
    }

    public AssertionClaimComparator<Date> birthdate() {
        return this.addComparatorAssertionClaim("birthdate");
    }

    public AssertionClaimSimple<String> gender() {
        return this.addSimpleAssertionClaim("gender");
    }

    public AssertionClaimSimple<String> countryOfBirth() {
        return this.addSimpleAssertionClaim("country_of_birth");
    }

    public AssertionClaimSimple<String> title() {
        return this.addSimpleAssertionClaim("title");
    }

    public AssertionClaimSimple<String> nationality() {
        return this.addSimpleAssertionClaim("nationality");
    }

    public AssertionClaimSimple<String> civilStatus() {
        return this.addSimpleAssertionClaim("civil_status");
    }

    public AssertionClaimComparator<Integer> age() {
        return this.addComparatorAssertionClaim("age");
    }

    public AssertionClaimSimple<String> companyRegisteredName() {
        return this.addSimpleAssertionClaim("company_registered_name");
    }

    public AssertionClaimSimple<String> companyTradeName() {
        return this.addSimpleAssertionClaim("company_trade_name");
    }

    public AssertionClaimComparator<Date> companyStartDate() {
        return this.addComparatorAssertionClaim("company_start_date");
    }

    public AssertionClaimComparator<Date> companyEndDate() {
        return this.addComparatorAssertionClaim("company_end_date");
    }

    public AssertionClaimSimple<String> companyType() {
        return this.addSimpleAssertionClaim("company_type");
    }

    public AssertionClaimSimple<String> companyCountryIncorporation() {
        return this.addSimpleAssertionClaim("company_country_incorporation");
    }

    public AssertionClaimComparator<Integer> companyAge() {
        return this.addComparatorAssertionClaim("company_age");
    }

    public AssertionClaimSimple<Boolean> companyOperating() {
        return this.addSimpleAssertionClaim("company_operating");
    }


    public AssertionClaimComplex address() {
        return this.addAssertionClaimComplex("address");
    }

    public AssertionClaimSimple<String> email() {
        return this.addSimpleAssertionClaim("email");
    }

    public AssertionClaimSimple<String> phoneNumber() {
        return this.addSimpleAssertionClaim("phone_number");
    }

    public AssertionClaimComplex totalBalance() {
        return this.addAssertionClaimComplex("total_balance");
    }
    public AssertionClaimComplex lastYearMoneyIn() {
        return this.addAssertionClaimComplex("last_year_money_in");
    }
    public AssertionClaimComplex lastQuarterMoneyIn() {
        return this.addAssertionClaimComplex("last_quarter_money_in");
    }
    public AssertionClaimComplex averageMonthlyMoneyIn() {
        return this.addAssertionClaimComplex("average_monthly_money_in");
    }

    public AssertionClaimSimple<String> passportId() {
        return this.addSimpleAssertionClaim("passport_id");
    }


    public AssertionClaimSimple<String> drivingLicenseId() {
        return this.addSimpleAssertionClaim("driving_license_id");
    }


    public AssertionClaimSimple<String> nationalCardId() {
        return this.addSimpleAssertionClaim("national_card_id");
    }



    public JSONObject toJSON() {
        final JSONObject jsonAssertionClaims = new JSONObject();
        try {
            for (AssertionClaim claim : this.claims) {
                jsonAssertionClaims.put(claim.getClaimName(), claim.toJSON());
            }
        } catch (JSONException e) {
            throw new DigitalIdGenericException(e);
        }
        return jsonAssertionClaims;
    }

    private Optional<AssertionClaim> getExistingClaim(final String claimName) {
        return this.claims.stream()
                .filter(claim -> claim.getClaimName().equals(claimName))
                .findFirst();
    }

    private <T> AssertionClaimSimple<T> addNewClaim(final AssertionClaimSimple<T> newClaim) {
        this.claims.add(newClaim);
        return newClaim;
    }

    protected <T> AssertionClaimSimple<T> addSimpleAssertionClaim(final String claimName) {
        getExistingClaim(claimName).ifPresent(this.claims::remove);

        final AssertionClaimSimple<T> newClaim = new AssertionClaimSimple<>(claimName);
        return addNewClaim(newClaim);
    }

    protected <T> AssertionClaimComparator<T> addComparatorAssertionClaim(final String claimName) {
        getExistingClaim(claimName).ifPresent(this.claims::remove);

        final AssertionClaimComparator<T> newClaim = new AssertionClaimComparator<>(claimName);
        this.claims.add(newClaim);
        return newClaim;
    }

    protected AssertionClaimComplex addAssertionClaimComplex(final String claimName) {
        final Optional<AssertionClaim> existingClaim = getExistingClaim(claimName);

        if (existingClaim.isPresent()) {
            return (AssertionClaimComplex) existingClaim.get();
        } else {
            final AssertionClaimComplex newClaim = new AssertionClaimComplex(claimName);
            this.claims.add(newClaim);
            return newClaim;
        }
    }
}
