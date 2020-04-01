package com.santander.digital.verifiedid.model.token;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AddressClaimResponse {

    @JsonProperty("formatted")
    private String formatted;

    @JsonProperty("postal_code")
    private String postalCode;

    @JsonProperty("street_address")
    private String streetAddress;

    @JsonProperty("locality")
    private String locality;

    @JsonProperty("region")
    private String region;

    @JsonProperty("country")
    private String country;


}
