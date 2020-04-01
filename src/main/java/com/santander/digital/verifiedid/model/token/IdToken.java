package com.santander.digital.verifiedid.model.token;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Builder
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class IdToken {

    @JsonProperty("aud")
    protected String audience;

    @JsonProperty("nonce")
    protected String nonce;

    @JsonProperty("sub")
    protected String sub;

    @JsonProperty("iss")
    protected String issuer;

    @JsonProperty("exp")
    protected Long expiration;

    @JsonProperty("iat")
    protected Long issuedAt;

    @JsonProperty("assertion_claims")
    private Map<String, AssertionClaimResponse> assertionClaims = new HashMap<>();

    @JsonProperty("family_name")
    private String familyName;

    @JsonProperty("given_name")
    private String givenName;

    @JsonProperty("birthdate")
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date birthDate;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("email")
    private String email;

    @JsonProperty("country_of_birth")
    private String countryOfBirth;

    @JsonProperty("title")
    private String title;

    @JsonProperty("nationality")
    private String nationality;

    @JsonProperty("civil_status")
    private String civilStatus;

    @JsonProperty("age")
    private Integer age;

    @JsonProperty("company_registered_name")
    private String companyRegisteredName;

    @JsonProperty("company_trade_name")
    private String companyTradeName;

    @JsonProperty("company_start_date")
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date companyStartDate;

    @JsonProperty("company_end_date")
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date companyEndDate;

    @JsonProperty("company_type")
    private String companyType;

    @JsonProperty("company_country_incorporation")
    private String companyCountryIncorporation;

    @JsonProperty("company_age")
    private Integer companyAge;

    @JsonProperty("company_operating")
    private Boolean companyOperating;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("address")
    private AddressClaimResponse address;

    @JsonProperty("total_balance")
    private BalanceClaimReponse totalBalanceReponse;

    @JsonProperty("last_year_money_in")
    private BalanceClaimReponse lastYearMoneyIn;

    @JsonProperty("last_quarter_money_in")
    private BalanceClaimReponse lastQuarterMoneyIn;

    @JsonProperty("average_monthly_money_in")
    private BalanceClaimReponse averageMonthlyMoneyIn;

    @JsonProperty("passport_id")
    private String passportId;

    @JsonProperty("driving_license_id")
    private String drivingLicenseId;

    @JsonProperty("national_card_id")
    private String nationalCardId;
}
