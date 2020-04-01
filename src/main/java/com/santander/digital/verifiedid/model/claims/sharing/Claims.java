package com.santander.digital.verifiedid.model.claims.sharing;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Claims {
    @Getter private final List<Claim> claims = new ArrayList<>();

    public Claim address() {
        return addClaim("address");
    }

    public Claim age() {
        return addClaim("age");
    }

    public Claim averageMonthlyMoneyIn() {
        return addClaim("average_monthly_money_in");
    }

    public Claim birthdate() {
        return addClaim("birthdate");
    }

    public Claim civilStatus() {
        return addClaim("civil_status");
    }

    public Claim companyAge() {
        return addClaim("company_age");
    }

    public Claim companyCountryIncorporation() {
        return addClaim("company_country_incorporation");
    }

    public Claim companyEndDate() {
        return addClaim("company_end_date");
    }

    public Claim companyOperating() {
        return addClaim("company_operating");
    }

    public Claim companyRegisteredName() {
        return addClaim("company_registered_name");
    }

    public Claim companyStartDate() {
        return addClaim("company_start_date");
    }

    public Claim companyTradeName() {
        return addClaim("company_trade_name");
    }

    public Claim companyType() {
        return addClaim("company_type");
    }

    public Claim countryOfBirth() {
        return addClaim("country_of_birth");
    }

    public Claim drivingLicenseId() {
        return addClaim("driving_license_id");
    }

    public Claim email() {
        return addClaim("email");
    }


    public Claim familyName() {
        return addClaim("family_name");
    }

    public Claim gender() {
        return addClaim("gender");
    }

    public Claim givenName() {
        return addClaim("given_name");
    }

    public Claim lastQuarterMoneyIn() {
        return addClaim("last_quarter_money_in");
    }

    public Claim lastYearMoneyIn() {
        return addClaim("last_year_money_in");
    }

    public Claim nationality() {
        return addClaim("nationality");
    }

    public Claim nationalCardId() {
        return addClaim("national_card_id");
    }

    public Claim passportId() {
        return addClaim("passport_id");
    }

    public Claim phoneNumber() {
        return addClaim("phone_number");
    }

    public Claim totalBalance() {
        return addClaim("total_balance");
    }

    public Claim title() {
        return addClaim("title");
    }

    protected Claim addClaim(final String type) {
        final Optional<Claim> emailClient = this.claims.stream()
                .filter(claim -> claim.getClaimName().equals(type))
                .findFirst();

        emailClient.ifPresent(claim -> this.claims.remove(claim));

        final Claim email = new Claim(type, this);
        this.claims.add(email);
        return email;
    }

    protected void removeClaim(final String claimName) {
        final Optional<Claim> emailClient = this.claims.stream()
                .filter(claim -> claim.getClaimName().equals(claimName))
                .findFirst();

        emailClient.ifPresent(this.claims::remove);
    }

}
